package noodlezip.store.repository;

import noodlezip.store.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByStoreId(Long storeId);
    // 매장 메뉴 조회할 때 사용
}
