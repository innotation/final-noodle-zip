package noodlezip.store.repository;

import noodlezip.store.entity.TblStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<TblStore, Long> {
}
