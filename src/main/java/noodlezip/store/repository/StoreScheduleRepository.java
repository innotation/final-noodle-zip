package noodlezip.store.repository;

import noodlezip.store.entity.TblStoreSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreScheduleRepository extends JpaRepository<TblStoreSchedule, Long> {
    List<TblStoreSchedule> findByStoreId(Long storeId);
    // 매장 메뉴 조회할 때 사용
}
