package noodlezip.store.service;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.dto.WeekScheduleDto;
import noodlezip.store.entity.Store;
import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.repository.StoreWeekScheduleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StoreService {

    private final StoreRepository storeRepository;
    private final StoreWeekScheduleRepository storeWeekScheduleRepository;

    @Transactional
    public Store saveStore(StoreRequestDto dto) {
        // 1️⃣ Store Entity 생성 및 데이터 세팅
        Store store = new Store();
        store.setStoreName(dto.getStoreName());
        store.setAddress(dto.getAddress());
        store.setPhone(dto.getPhone());
        store.setIsLocalCard(dto.getIsLocalCard());
        store.setIsChildAllowed(dto.getIsChildAllowed());
        store.setHasParking(dto.getHasParking());
        store.setOperationStatus(dto.getOperationStatus());
        store.setOwnerComment(dto.getOwnerComment());
        store.setStoreMainImageUrl(dto.getStoreMainImageUrl());
        store.setXAxis(dto.getXAxis());
        store.setYAxis(dto.getYAxis());
        store.setCreatedAt(LocalDateTime.now());
        store.setUpdatedAt(LocalDateTime.now());

        // 2️⃣ 먼저 store 저장 → storeId 획득
        Store savedStore = storeRepository.save(store);

        // 3️⃣ 요일별 스케줄 생성 후 저장
        List<StoreWeekSchedule> scheduleList = new ArrayList<>();

        for (WeekScheduleDto weekDto : dto.getWeekSchedules()) {
            StoreWeekSchedule schedule = new StoreWeekSchedule();

            // 복합키 생성
            StoreWeekScheduleId scheduleId = new StoreWeekScheduleId();
            scheduleId.setStoreId(savedStore.getStoreId());
            scheduleId.setDayOfWeek(weekDto.getDayOfWeek());

            schedule.setId(scheduleId);
            schedule.setOpeningAt(weekDto.getOpeningAt());
            schedule.setClosingAt(weekDto.getClosingAt());
            schedule.setIsClosedDay(weekDto.getIsClosedDay());
            schedule.setStore(savedStore);

            scheduleList.add(schedule);
        }

        // 저장
        storeWeekScheduleRepository.saveAll(scheduleList);

        return savedStore;
    }
}
