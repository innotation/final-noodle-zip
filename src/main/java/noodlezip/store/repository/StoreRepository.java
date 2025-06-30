package noodlezip.store.repository;

import noodlezip.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {
    // 기본 CRUD, 페이징, 정렬 등은 JpaRepository에서 제공
}
