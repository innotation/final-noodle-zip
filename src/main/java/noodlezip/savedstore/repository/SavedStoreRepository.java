package noodlezip.savedstore.repository;

import noodlezip.savedstore.entity.SavedStore;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SavedStoreRepository extends JpaRepository<SavedStore, Long> , SaveStoreQueryRepository{

    void deleteByUserIdAndStoreId(long userId, long storeId);

    boolean existsByUserIdAndStoreId(long userId, long storeId);

    Optional<SavedStore> findByUserIdAndStoreId(long userId, long storeId);

    // 여러 결과를 반환하는 메서드 추가
    List<SavedStore> findAllByUserIdAndStoreId(long userId, long storeId);

}