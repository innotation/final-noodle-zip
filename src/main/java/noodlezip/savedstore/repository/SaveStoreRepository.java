package noodlezip.savedstore.repository;

import noodlezip.savedstore.entity.SaveStore;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveStoreRepository extends JpaRepository<SaveStore, Long> {

    void deleteByUserIdAndStoreId(long userId, long storeId);

}