package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.*;
import noodlezip.store.entity.*;
import noodlezip.store.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreScheduleRepository scheduleRepository;
    private final MenuRepository menuRepository;

    @Transactional
    public Long registerStore(StoreRequestDto dto) {
        TblStore store = TblStore.builder()
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
                .build();

        TblStore savedStore = storeRepository.save(store);

        // 요일별 영업시간 저장
        List<TblStoreSchedule> schedules = dto.getWeekSchedule().stream()
                .map(s -> TblStoreSchedule.builder()
                        .storeId(savedStore.getId())
                        .dayOfWeek(s.getDayOfWeek())
                        .openingAt(s.getOpeningAt())
                        .closingAt(s.getClosingAt())
                        .isClosedDay(s.getIsClosedDay())
                        .build())
                .collect(Collectors.toList());
        scheduleRepository.saveAll(schedules);

        // 메뉴 저장
        List<TblMenu> menus = dto.getMenus().stream()
                .map(m -> TblMenu.builder()
                        .storeId(savedStore.getId())
                        .menuName(m.getMenuName())
                        .price(m.getPrice())
                        .menuDescription(m.getMenuDescription())
                        .menuImageUrl(m.getMenuImageUrl())
                        .ramenCategoryId(m.getRamenCategoryId())
                        .ramenSoupId(m.getRamenSoupId())
                        .build())
                .collect(Collectors.toList());
        menuRepository.saveAll(menus);

        return savedStore.getId();
    }
}
