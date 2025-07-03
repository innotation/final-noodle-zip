package noodlezip.store.repository;

import noodlezip.admin.dto.RegistListDto;
import noodlezip.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

        @Query(
                value = """
        SELECT  u.login_id,
                s.store_name                 AS storeName,
                DATE_FORMAT(s.created_at,
                            '%Y-%m-%d %H:%i:%s') AS createdAt
        FROM    tbl_store s JOIN tbl_user u ON u.user_id = s.user_id
        WHERE   s.approval_status = 'WAITING'
        """,
                nativeQuery = true)
        Page<RegistListDto> findRegistStores(Pageable pageable);
}
