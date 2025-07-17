package noodlezip.store.repository;

import noodlezip.store.entity.Store;
import noodlezip.store.entity.StoreWeekSchedule;
import noodlezip.store.entity.StoreWeekScheduleId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreWeekScheduleRepository extends JpaRepository<StoreWeekSchedule, StoreWeekScheduleId> {
    void deleteByStore(Store store);
    List<StoreWeekSchedule> findByStoreId(Long storeId);
}
