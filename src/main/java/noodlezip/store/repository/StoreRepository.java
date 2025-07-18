package noodlezip.store.repository;

import jakarta.validation.constraints.NotNull;
import noodlezip.admin.dto.RegistListDto;
import noodlezip.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {

    Long getStoreByBizNum(@NotNull Long bizNum);
    List<Store> findAllByUserId(Long userId);
}
