package noodlezip.store.repository;

import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreWeekScheduleRepository extends JpaRepository<StoreWeekSchedule, StoreWeekScheduleId> {
    // 복합키가 있는 경우 PK 타입에 맞게 제네릭 선언
}
