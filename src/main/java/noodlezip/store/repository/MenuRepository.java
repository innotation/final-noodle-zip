package noodlezip.store.repository;

import noodlezip.store.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long> {
    // 예: 특정 매장의 메뉴 리스트 조회
    List<Menu> findByStoreId(Long storeId);
}