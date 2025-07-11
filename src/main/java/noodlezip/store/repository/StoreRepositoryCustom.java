package noodlezip.store.repository;

import noodlezip.admin.dto.RegistListDto;
import noodlezip.search.dto.SearchFilterDto;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.store.dto.StoreIdNameDto;
import noodlezip.store.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StoreRepositoryCustom {

    Page<SearchStoreDto> findStoreOrderByDistance(double lat, double lng, Pageable pageable);

    Page<SearchStoreDto> searchStoresByFilter(SearchFilterDto filter, Pageable pageable);

    Page<RegistListDto> findWaitingStores(Pageable pageable);

    List<String> findDistinctCategories();

    List<String> findDistinctSoups();

    List<String> findDistinctToppings();

    List<StoreIdNameDto> findIdNameByBizNum(Long bizNum);
}
