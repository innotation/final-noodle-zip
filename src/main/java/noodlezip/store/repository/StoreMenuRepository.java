package noodlezip.store.repository;

import noodlezip.store.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreMenuRepository extends JpaRepository<Menu, Long> {
    // 필요하면 커스텀 쿼리 메서드 추가 가능
}
