package noodlezip.store.repository;

import jakarta.persistence.criteria.CriteriaBuilder;
import noodlezip.store.entity.Menu;
import noodlezip.store.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, MenuRepositoryCustom {

    boolean existsByStoreIdAndMenuName(Long storeId, String menuName);

    void deleteByStore(Store store);
  
    List<Menu> findByStore(Store store);

    @Query("SELECT m.category.id FROM Menu m WHERE m.id IN :menuIds")
    List<Integer> findAllCategoryIdByMenuIds(List<Long> menuIds);

}
