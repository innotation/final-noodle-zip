package noodlezip.store.repository;

import noodlezip.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    // 필요한 커스텀 쿼리 있으면 추가
}