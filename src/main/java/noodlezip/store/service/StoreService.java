package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.RamenCategory;
import noodlezip.ramen.entity.RamenSoup;
import noodlezip.ramen.entity.RamenTopping;
import noodlezip.ramen.entity.RamenToppingId;
import noodlezip.ramen.service.RamenService;
import noodlezip.ramen.repository.RamenToppingRepository;
import noodlezip.store.dto.MenuRequestDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.dto.StoreScheduleRequestDto;
import noodlezip.store.entity.Menu;
import noodlezip.store.entity.Store;
import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.repository.StoreScheduleRepository;
import noodlezip.users.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreScheduleRepository scheduleRepository;
    private final MenuRepository menuRepository;
    private final RamenToppingRepository ramenToppingRepository;

    private final RamenService ramenService;

    /* 가게 등록 (userId 포함) */
    @Transactional
    public Long registerStore(StoreRequestDto dto, Long userId) {
        // user 엔티티 생성
        User user = new User();
        user.setId(userId);

        // 가게 저장
        Store store = Store.builder()
                .storeName(dto.getStoreName())
                .address(dto.getAddress())
                .phone(dto.getPhone())
                .isLocalCard(dto.getIsLocalCard())
                .isChildAllowed(dto.getIsChildAllowed())
                .hasParking(dto.getHasParking())
                .ownerComment(dto.getOwnerComment())
                .storeMainImageUrl(dto.getStoreMainImageUrl())
                .xAxis(dto.getXAxis())
                .yAxis(dto.getYAxis())
                .user(user)
                .build();

        Store savedStore = storeRepository.save(store);

        // 영업시간 저장
        if (dto.getWeekSchedule() != null) {
            List<StoreWeekSchedule> schedules = dto.getWeekSchedule().stream()
                    .map(s -> {
                        StoreWeekScheduleId id = new StoreWeekScheduleId();
                        id.setStoreId(savedStore.getId());
                        id.setDayOfWeek(s.getDayOfWeek());

                        StoreWeekSchedule schedule = new StoreWeekSchedule();
                        schedule.setId(id);
                        schedule.setOpeningAt(LocalDateTime.of(LocalDate.now(), s.getOpeningAt()));
                        schedule.setClosingAt(LocalDateTime.of(LocalDate.now(), s.getClosingAt()));
                        schedule.setIsClosedDay(s.getIsClosedDay());
                        return schedule;
                    })
                    .collect(Collectors.toList());

            scheduleRepository.saveAll(schedules);
        }

        // 메뉴 및 기본 토핑 저장
        if (dto.getMenus() != null) {
            for (MenuRequestDto m : dto.getMenus()) {
                RamenCategory category = new RamenCategory();
                category.setId(m.getRamenCategoryId());

                RamenSoup soup = new RamenSoup();
                soup.setId(m.getRamenSoupId());

                Menu menu = Menu.builder()
                        .store(savedStore)
                        .menuName(m.getMenuName())
                        .price(m.getPrice())
                        .menuDescription(m.getMenuDescription())
                        .menuImageUrl(m.getMenuImageUrl())
                        .ramenCategory(category)
                        .ramenSoup(soup)
                        .build();

                Menu savedMenu = menuRepository.save(menu);

                if (m.getDefaultToppingIds() != null) {
                    for (Long toppingId : m.getDefaultToppingIds()) {
                        RamenToppingId toppingIdObj = new RamenToppingId();
                        toppingIdObj.setMenuId(savedMenu.getId());
                        toppingIdObj.setToppingId(toppingId);

                        RamenTopping ramenTopping = new RamenTopping();
                        ramenTopping.setId(toppingIdObj);

                        ramenToppingRepository.save(ramenTopping);
                    }
                }
            }
        }

        return savedStore.getId();
    }

    /* 테스트용 userId 없이 등록 (임시) */
    @Transactional(readOnly = true)
    public Long registerStore(StoreRequestDto dto) {
        return registerStore(dto, 1L); // 임시: userId = 1
    }

    /* 라멘 카테고리 목록 조회 */
    public List<CategoryResponseDto> getRamenCategories() {
        return ramenService.getAllCategories();
    }

    /* 라멘 토핑 목록 조회 */
    public List<ToppingResponseDto> getRamenToppings() {
        return ramenService.getAllToppings();
    }
}