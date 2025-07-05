package noodlezip.savedstore.repository;

import noodlezip.savedstore.entity.SavedStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedStoreRepository extends JpaRepository<SavedStore, Long> {

    void deleteByUserIdAndStoreId(long userId, long storeId);

    boolean existsByUserIdAndStoreId(long userId, long storeId);

    Optional<SavedStore> findByUserIdAndStoreId(long userId, long storeId);

}