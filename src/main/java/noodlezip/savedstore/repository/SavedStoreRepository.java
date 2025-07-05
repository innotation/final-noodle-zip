package noodlezip.savedstore.repository;

import noodlezip.savedstore.entity.SavedStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedStoreRepository extends JpaRepository<SavedStore, Long> {

    void deleteByUserIdAndStoreId(long userId, long storeId);

}