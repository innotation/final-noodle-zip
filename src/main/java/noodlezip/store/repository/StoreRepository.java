package noodlezip.store.repository;

import jakarta.validation.constraints.NotNull;
import noodlezip.admin.dto.RegistListDto;
import noodlezip.store.entity.Store;
import noodlezip.store.status.OperationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StoreRepository extends JpaRepository<Store, Long>, StoreRepositoryCustom {
    List<Store> findByUserIdAndOperationStatusNot(Long userId, OperationStatus operationStatus);

    Long getStoreByBizNum(@NotNull Long bizNum);
    List<Store> findAllByUserId(Long userId);

}
