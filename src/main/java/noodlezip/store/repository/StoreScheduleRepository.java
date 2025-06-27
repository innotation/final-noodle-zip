package noodlezip.store.repository;

import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StoreScheduleRepository extends JpaRepository<StoreWeekSchedule, StoreWeekScheduleId> {
    // 예: 특정 매장의 영업시간 리스트 조회
    List<StoreWeekSchedule> findByIdStoreId(Long storeId);
}