package noodlezip.store.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.admin.dto.RegistListDto;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.FileUtil;
import noodlezip.common.util.PageUtil;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.RamenSoupResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.Category;
import noodlezip.ramen.entity.RamenSoup;
import noodlezip.ramen.entity.RamenTopping;
import noodlezip.ramen.entity.Topping;
import noodlezip.ramen.repository.RamenReviewRepository;
import noodlezip.ramen.repository.RamenToppingRepository;
import noodlezip.ramen.repository.ReviewToppingRepository;
import noodlezip.ramen.repository.ToppingRepository;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.*;
import noodlezip.store.entity.*;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreExtraToppingRepository;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.repository.StoreWeekScheduleRepository;
import noodlezip.store.status.ApprovalStatus;
import noodlezip.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import noodlezip.store.status.StoreErrorCode;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreWeekScheduleRepository scheduleRepository;
    private final MenuRepository menuRepository;
    private final RamenToppingRepository ramenToppingRepository;
    private final RamenService ramenService;
    private final ModelMapper modelMapper;
    private final PageUtil pageUtil;
    private final ToppingRepository toppingRepository;
    private final FileUtil fileUtil;
    private final EntityManager em;
    private final RamenReviewRepository ramenReviewRepository;
    private final ReviewToppingRepository reviewToppingRepository;
    private final StoreExtraToppingRepository storeExtraToppingRepository;

    @Transactional(rollbackFor = Exception.class)
    public Long registerStore(StoreRequestDto dto, MultipartFile storeMainImage, User user) {

        List<String> defaultToppingNames = toppingRepository.findAll().stream()
                .filter(Topping::getIsActive)
                .map(Topping::getToppingName)
                .toList();

        String storeMainImageUrl = null;

        // ① 대표 이미지 업로드 및 유효성 검사
        if (storeMainImage != null && !storeMainImage.isEmpty()) {
            try {
                Map<String, String> uploadResult = fileUtil.fileupload("storeRegist/0708", storeMainImage);
                storeMainImageUrl = uploadResult.get("fileUrl");
            } catch (CustomException ce) {
                throw ce;
            } catch (Exception e) {
                throw new CustomException(ErrorStatus._FILE_UPLOAD_FAILED);
            }
        } else {
            throw new CustomException(ErrorStatus._FILE_REQUIRED);
        }

        // ② Store 엔티티 생성 및 저장
        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .bizNum(dto.getBizNum())
                .isLocalCard(dto.getIsLocalCard())
                .isChildAllowed(dto.getIsChildAllowed())
                .hasParking(dto.getHasParking())
                .ownerComment(dto.getOwnerComment())
                .storeMainImageUrl(storeMainImageUrl)
                .storeLat(dto.getStoreLat())
                .storeLng(dto.getStoreLng())
                .approvalStatus(ApprovalStatus.WAITING)
                .operationStatus(dto.getOperationStatus())
                .userId(user.getId())
                .build();
        store.setStoreLegalCode(dto.getStoreLegalCode() != null ? dto.getStoreLegalCode().longValue() : null);

        Store savedStore = storeRepository.save(store);

        // ③ 영업시간 저장
        if (dto.getWeekSchedule() != null) {
            List<StoreWeekSchedule> schedules = dto.getWeekSchedule().stream()
                    .map(s -> {
                        StoreWeekScheduleId id = new StoreWeekScheduleId(savedStore.getId(), s.getDayOfWeek());
                        StoreWeekSchedule schedule = new StoreWeekSchedule();
                        schedule.setId(id);
                        schedule.setIsClosedDay(s.getIsClosedDay());
                        if (Boolean.TRUE.equals(s.getIsClosedDay())) {
                            schedule.setOpeningAt(LocalTime.MIN); // 00:00
                            schedule.setClosingAt(LocalTime.MIN); // 00:00
                        } else {
                            schedule.setOpeningAt(s.getOpeningAt());
                            schedule.setClosingAt(s.getClosingAt());
                        }

                        schedule.setStoreId(savedStore);

                        return schedule;
                    }).collect(Collectors.toList());

            scheduleRepository.saveAll(schedules);
        }

        // ④ 메뉴 및 기본 토핑, 추가 토핑 저장
        if (dto.getMenus() != null) {
            for (MenuRequestDto m : dto.getMenus()) {
                // 메뉴 이름 중복 체크 (해당 매장(store) 내에 중복 이름 있는지 검사)
                boolean exists = menuRepository.existsByStoreIdAndMenuName(savedStore.getId(), m.getMenuName());
                if (exists) {
                    throw new CustomException(StoreErrorCode._DUPLICATE_MENU_NAME);
                }
                String menuImageUrl = null;
                MultipartFile menuImageFile = m.getMenuImageFile();

                // 메뉴 이미지 업로드
                if (menuImageFile != null && !menuImageFile.isEmpty()) {
                    try {
                        Map<String, String> uploadResult = fileUtil.fileupload("storeRegist/0708", menuImageFile);
                        menuImageUrl = uploadResult.get("fileUrl"); // 전체 URL 저장
                    } catch (CustomException ce) {
                        throw ce;
                    } catch (Exception e) {
                        throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
                    }
                }

                // 클라이언트에서 직접 URL 보낸 경우 사용
                if (menuImageUrl == null) {
                    throw new CustomException(ErrorStatus._FILE_REQUIRED);
                }

                // 카테고리/육수 매핑
                Category category = new Category();
                category.setId(m.getRamenCategoryId());

                RamenSoup soup = new RamenSoup();
                soup.setId(m.getRamenSoupId());

                // 메뉴 엔티티 저장
                Menu menu = Menu.builder()
                        .store(savedStore)
                        .menuName(m.getMenuName())
                        .price(m.getPrice())
                        .menuDescription(m.getMenuDescription())
                        .menuImageUrl(menuImageUrl)
                        .category(category)
                        .ramenSoup(soup)
                        .build();

                Menu savedMenu = menuRepository.save(menu);

                // 기본 토핑 저장 (메뉴 단위)
                if (m.getDefaultToppingIds() != null) {
                    for (Long toppingId : m.getDefaultToppingIds()) {
                        Topping topping = toppingRepository.getReferenceById(toppingId);
                        RamenTopping ramenTopping = new RamenTopping();
                        ramenTopping.setMenu(savedMenu);
                        ramenTopping.setTopping(topping);
                        ramenToppingRepository.save(ramenTopping);
                    }
                }
            }
        }

        // ⑤ 매장 단위 추가 토핑 저장 (dto.getExtraToppings)
        if (dto.getExtraToppings() != null && !dto.getExtraToppings().isEmpty()) {
            for (ExtraToppingRequestDto toppingDto : dto.getExtraToppings()) {
                if (toppingDto == null || toppingDto.getName() == null || toppingDto.getName().isBlank()) continue;

                String toppingName = toppingDto.getName();
                Integer price = toppingDto.getPrice() != null ? toppingDto.getPrice() : 0;

                if (defaultToppingNames.contains(toppingName)) {
                    throw new CustomException(StoreErrorCode._CANNOT_USE_DEFAULT_TOPPING);
                }

                Topping topping = toppingRepository.findByToppingName(toppingName)
                        .orElseGet(() -> toppingRepository.save(
                                Topping.builder()
                                        .toppingName(toppingName)
                                        .isActive(false)
                                        .build()
                        ));

                if (topping == null) {
                    throw new CustomException(StoreErrorCode._UNKNOWN_TOPPING_NAME);
                }

                StoreExtraTopping storeExtraTopping = new StoreExtraTopping();
                storeExtraTopping.setStore(savedStore);
                storeExtraTopping.setTopping(topping);
                storeExtraTopping.setPrice(price);
                storeExtraToppingRepository.save(storeExtraTopping);
            }
        }

        return savedStore.getId();
    }

    // 라멘 육수 목록 조회
    public List<RamenSoupResponseDto> getAllSoups() {
        return ramenService.getAllSoups();
    }

    // 라멘 카테고리 목록 조회
    public List<CategoryResponseDto> getRamenCategories() {
        return ramenService.getAllCategories();
    }

    // 라멘 토핑 목록 조회
    public List<ToppingResponseDto> getRamenToppings() {
        return ramenService.getAllToppings();
    }

    // 등록요청매장 조회
    public Map<String, Object> findWaitingStores(Pageable pageable) {
        Page<RegistListDto> resultPage = storeRepository.findWaitingStores(pageable);
        Map<String, Object> map = pageUtil.getPageInfo(resultPage, 5);
        map.put("registList", resultPage.getContent());
        return map;
    }

    // 등록 요청 매장 상태 변경
    public void changeStatus(Long id, ApprovalStatus status) {
        Store store = storeRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorStatus._DATA_NOT_FOUND));
        store.setApprovalStatus(status);
    }

    // ID로 활성화 된 매장 찾기
    public StoreDto getStore(Long storeId, Long requesterUserId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException("해당 매장을 찾을 수 없습니다."));

        // 승인되지 않았고, 등록한 사용자가 아닐 경우만 막기
        if (!ApprovalStatus.APPROVED.equals(store.getApprovalStatus()) &&
                (!store.getUserId().equals(requesterUserId))) {
            throw new IllegalStateException("승인되지 않은 매장은 조회할 수 없습니다.");
        }

        return StoreDto.toDto(store);
    }

    // 매장에서 메뉴 조회
    public MenuDetailResponseDto getMenuDetail(Long storeId) {

        List<MenuDetailDto> menuList = menuRepository.findMenuDetailByStoreId(storeId);
        Map<Long, List<String>> toppingMap = ramenToppingRepository.findToppingNamesByStoreGroupedByMenuId(storeId);

        // 메뉴에 라멘토핑 맵핑
        for (MenuDetailDto dto : menuList) {
            List<String> toppings = toppingMap.getOrDefault(dto.getMenuId(), List.of());
            dto.setToppingNames(toppings);
        }

        List<String> categories = ramenService.extractUniqueCategories(menuList);
        List<String> soups = ramenService.extractUniqueSoups(menuList);
        List<String> toppings = ramenService.extractUniqueToppings(menuList);

        return MenuDetailResponseDto.builder()
                .menus(menuList)
                .categoryNames(categories)
                .soupNames(soups)
                .toppingNames(toppings)
                .build();
    }

    // 매장 리뷰 조회
    public Page<StoreReviewDto> getReviews(Long storeId, Pageable pageable) {
        // 1. 리뷰 + 메뉴 페이지 조회
        Page<StoreReviewDto> page = ramenReviewRepository.findReviewsByStoreId(storeId, pageable);
        List<StoreReviewDto> dtoList = page.getContent();

        // 2. 리뷰 ID 목록 추출
        List<Long> reviewIds = dtoList.stream()
                .map(StoreReviewDto::getId)
                .toList();

        // 3. 토핑 조회
        Map<Long, List<String>> toppingMap = reviewToppingRepository.findToppingNamesByReviewIds(reviewIds);

        // 4. 병합
        dtoList.forEach(dto -> dto.setToppingNames(
                toppingMap.getOrDefault(dto.getId(), List.of())
        ));

        return new PageImpl<>(dtoList, pageable, page.getTotalElements());
    }

    // 매장 토핑 조회
    public List<ToppingResponseDto> getStoreToppings(Long storeId) {
        Store store = em.getReference(Store.class, storeId);
        List<ToppingResponseDto> result = new ArrayList<>();
        storeExtraToppingRepository.findStoreExtraToppingByStore(store)
                .forEach(topping -> {
                    try {
                        result.add(new ToppingResponseDto(
                                        topping.getId(),
                                        topping.getTopping().getToppingName(),
                                        topping.getPrice()
                                )
                        );
                    } catch (NullPointerException e) {
                        throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
                    }
                });
        return result;
    }
}
