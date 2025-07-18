package noodlezip.ramen.repository;

import noodlezip.ramen.entity.RamenTopping;
import noodlezip.ramen.entity.RamenToppingId;
import noodlezip.store.entity.Menu;
import noodlezip.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RamenToppingRepository extends JpaRepository<RamenTopping, RamenToppingId>, RamenToppingRepositoryCustom {
    void deleteByMenu_Store(Store store);
    void deleteByMenu(Menu menu);

    // 메뉴 ID 리스트를 받아서 각 메뉴별 토핑 ID 리스트를 반환 (메뉴ID -> 토핑ID 리스트 매핑)
    @Query("SELECT rt.menu.id AS menuId, rt.topping.id AS toppingId FROM RamenTopping rt WHERE rt.menu.id IN :menuIds")
    List<Object[]> findMenuIdAndToppingIdByMenuIds(@Param("menuIds") List<Long> menuIds);
}