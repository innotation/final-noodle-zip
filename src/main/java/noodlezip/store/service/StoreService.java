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
import noodlezip.ramen.entity.Category;
import noodlezip.ramen.entity.RamenSoup;
import noodlezip.ramen.entity.RamenTopping;
import noodlezip.ramen.entity.Topping;
import noodlezip.ramen.repository.RamenToppingRepository;
import noodlezip.ramen.repository.ToppingRepository;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.dto.MenuRequestDto;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.entity.*;
import noodlezip.store.repository.*;
import noodlezip.user.entity.User;
import noodlezip.store.entity.Menu;
import noodlezip.store.entity.Store;
import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.repository.StoreWeekScheduleRepository;
import noodlezip.store.status.ApprovalStatus;
import noodlezip.user.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
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


    @Value("${upload.path}")
    private String uploadDir;

    @Value("${upload.url.prefix}")
    private String urlPrefix;

    @Transactional(rollbackFor = Exception.class)
    public Long registerStore(StoreRequestDto dto, Long userId, MultipartFile storeMainImage) throws IOException {
        User user = new User();
        user.setId(userId);

        String imageUrl = null;
        if (storeMainImage != null && !storeMainImage.isEmpty()) {
            imageUrl = saveFile(storeMainImage);
        }


        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isLocalCard(dto.getIsLocalCard())
                .isChildAllowed(dto.getIsChildAllowed())
                .hasParking(dto.getHasParking())
                .ownerComment(dto.getOwnerComment())
                .storeMainImageUrl(imageUrl)
                .storeLat(dto.getStoreLat())
                .storeLng(dto.getStoreLng())
                .approvalStatus(dto.getApprovalStatus())
                .operationStatus(dto.getOperationStatus())
                .userId(userId)
                .build();

        store.setStoreLegalCode(dto.getStoreLegalCode());

        Store savedStore = storeRepository.save(store);

        if (dto.getWeekSchedule() != null) {
            List<StoreWeekSchedule> schedules = dto.getWeekSchedule().stream()
                    .map(s -> {
                        StoreWeekScheduleId id = new StoreWeekScheduleId();
                        id.setStoreId(savedStore.getId());
                        id.setDayOfWeek(s.getDayOfWeek());

                        StoreWeekSchedule schedule = new StoreWeekSchedule();
                        schedule.setId(id);
                        schedule.setOpeningAt(s.getOpeningAt());
                        schedule.setClosingAt(s.getClosingAt());
                        schedule.setIsClosedDay(s.getIsClosedDay());
                        return schedule;
                    }).collect(Collectors.toList());

            scheduleRepository.saveAll(schedules);
        }

        // 메뉴 및 기본 토핑 저장
        if (dto.getMenus() != null) {
            for (MenuRequestDto m : dto.getMenus()) {
                RamenSoup soup = new RamenSoup();
                soup.setId(m.getRamenSoupId());

                Category category = new Category();
                category.setId(m.getRamenCategoryId());

                Menu menu = Menu.builder()
                        .storeId(savedStore)
                        .menuName(m.getMenuName())
                        .price(m.getPrice())
                        .menuDescription(m.getMenuDescription())
                        .menuImageUrl(m.getMenuImageUrl())
                        .category(category)
                        .ramenSoup(soup)
                        .build();

                Menu savedMenu = menuRepository.save(menu);

                if (m.getDefaultToppingIds() != null) {
                    for (Long toppingId : m.getDefaultToppingIds()) {
                        Topping topping = toppingRepository.getReferenceById(toppingId);

                        RamenTopping ramenTopping = new RamenTopping();
                        ramenTopping.setTopping(topping);
                        ramenTopping.setMenu(savedMenu);

                        ramenToppingRepository.save(ramenTopping);
                    }
                }
            }
        }

        return savedStore.getId();
    }

    // 삭제 메서드는 추가 기능(FileUtil에 추가 필요)

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
        Map<String, Object> map = pageUtil.getPageInfo(resultPage, resultPage.getSize());
        map.put("registList", resultPage.getContent());
        return map;
    }

    // ID로 활성화 된 매장 찾기
    public StoreDto getStore(Long storeId) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new NoSuchElementException("해당 매장을 찾을 수 없습니다."));
        if (!ApprovalStatus.APPROVED.equals(store.getApprovalStatus())) {
            throw new IllegalStateException("승인되지 않은 매장은 조회할 수 없습니다.");
        }

        return StoreDto.toDto(store);
    }

    // 매장에서 메뉴 조회
    public List<MenuDetailDto> getMenus(Long storeId) {
        List<MenuDetailDto> menuList = menuRepository.findMenuDetailByStoreId(storeId);
        Map<Long, List<String>> toppingMap = ramenToppingRepository.findToppingNamesByStoreGroupedByMenuId(storeId);

        // 메뉴에 라멘토핑 맵핑
        for (MenuDetailDto dto : menuList) {
            List<String> toppings = toppingMap.getOrDefault(dto.getMenuId(), List.of());
            dto.setToppingNames(toppings);
        }

        return menuList;
    }


}
