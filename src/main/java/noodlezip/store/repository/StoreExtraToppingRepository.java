package noodlezip.store.repository;

import noodlezip.store.entity.Store;
import noodlezip.store.entity.StoreExtraTopping;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoreExtraToppingRepository extends JpaRepository<StoreExtraTopping, Long> {
    List<StoreExtraTopping> findStoreExtraToppingByStore(Store store);
    void deleteByStore(Store store);
}