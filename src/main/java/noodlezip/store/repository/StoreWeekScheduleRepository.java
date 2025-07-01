package noodlezip.store.repository;

import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreWeekScheduleRepository extends JpaRepository<StoreWeekSchedule, StoreWeekScheduleId> {
}
