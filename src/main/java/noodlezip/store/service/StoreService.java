package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.admin.dto.RegistListDto;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.FileUtil;
import noodlezip.common.util.PageUtil;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.*;
import noodlezip.ramen.repository.RamenToppingRepository;
import noodlezip.ramen.repository.ToppingRepository;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.MenuRequestDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.entity.*;
import noodlezip.store.repository.*;
import noodlezip.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
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

    @Transactional(rollbackFor = Exception.class)
    public Long registerStore(StoreRequestDto dto, MultipartFile storeMainImage, User user) {
        String storeMainImageUrl = null;

        // 대표 이미지 로컬 저장
        if (storeMainImage != null && !storeMainImage.isEmpty()) {
            try {
                Map<String, String> uploadResult = fileUtil.fileupload("store", storeMainImage);
                // filePath + "/" + filesystemName 조합해서 URL/경로 만듦
                storeMainImageUrl = uploadResult.get("filePath") + "/" + uploadResult.get("filesystemName");
                log.info("Store main image uploaded for user {}: {}", user.getId(), storeMainImageUrl);
            } catch (Exception e) {
                log.error("Failed to upload store main image for user {}: {}", user.getId(), e.getMessage(), e);
                throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
            }
        }

        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isLocalCard(dto.getIsLocalCard())
                .isChildAllowed(dto.getIsChildAllowed())
                .hasParking(dto.getHasParking())
                .ownerComment(dto.getOwnerComment())
                .storeMainImageUrl(storeMainImageUrl)
                .storeLat(dto.getStoreLat())
                .storeLng(dto.getStoreLng())
                .approvalStatus(dto.getApprovalStatus())
                .operationStatus(dto.getOperationStatus())
                .userId(user.getId())
                .build();
        store.setStoreLegalCode(dto.getStoreLegalCode());

        Store savedStore = storeRepository.save(store);

        // 영업시간 저장
        if (dto.getWeekSchedule() != null) {
            List<StoreWeekSchedule> schedules = dto.getWeekSchedule().stream()
                    .map(s -> {
                        StoreWeekScheduleId id = new StoreWeekScheduleId(savedStore.getId(), s.getDayOfWeek());
                        StoreWeekSchedule schedule = new StoreWeekSchedule();
                        schedule.setId(id);
                        schedule.setIsClosedDay(s.getIsClosedDay());
                        if (Boolean.TRUE.equals(s.getIsClosedDay())) {
                            schedule.setOpeningAt(null);
                            schedule.setClosingAt(null);
                        } else {
                            schedule.setOpeningAt(s.getOpeningAt());
                            schedule.setClosingAt(s.getClosingAt());
                        }
                        return schedule;
                    }).collect(Collectors.toList());

            scheduleRepository.saveAll(schedules);
        }

        // 메뉴 및 기본 토핑 저장 (메뉴 이미지 로컬 저장 포함)
        if (dto.getMenus() != null) {
            for (MenuRequestDto m : dto.getMenus()) {
                String menuImageUrl = null;

                MultipartFile menuImageFile = m.getMenuImageFile();
                if (menuImageFile != null && !menuImageFile.isEmpty()) {
                    try {
                        Map<String, String> uploadResult = fileUtil.fileupload("menu", menuImageFile);
                        menuImageUrl = uploadResult.get("filePath") + "/" + uploadResult.get("filesystemName");
                        log.info("Menu image uploaded for menu {}: {}", m.getMenuName(), menuImageUrl);
                    } catch (Exception e) {
                        log.error("Failed to upload menu image for menu {}: {}", m.getMenuName(), e.getMessage(), e);
                        throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
                    }
                }

                if (menuImageUrl == null) {
                    menuImageUrl = m.getMenuImageUrl();  // 클라이언트가 이미 URL 넘겼을 경우
                }

                Category category = new Category();
                category.setId(m.getRamenCategoryId());

                RamenSoup soup = new RamenSoup();
                soup.setId(m.getRamenSoupId());

                Menu menu = Menu.builder()
                        .storeId(savedStore)
                        .menuName(m.getMenuName())
                        .price(m.getPrice())
                        .menuDescription(m.getMenuDescription())
                        .menuImageUrl(menuImageUrl)
                        .category(category)
                        .ramenSoup(soup)
                        .build();

                Menu savedMenu = menuRepository.save(menu);

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

        return savedStore.getId();
    }

    // 삭제 메서드는 추가 기능(FileUtil에 추가 필요)

    public List<CategoryResponseDto> getRamenCategories() {
        return ramenService.getAllCategories();
    }

    public List<ToppingResponseDto> getRamenToppings() {
        return ramenService.getAllToppings();
    }

    public Map<String, Object> findRegistList(Pageable pageable) {
        Page<RegistListDto> page = storeRepository.findRegistStores(pageable);
        Map<String, Object> map = pageUtil.getPageInfo(page, 5);
        map.put("registList", page.getContent());
        return map;
    }
}
