package noodlezip.store.repository;

import noodlezip.admin.dto.RegistListDto;
import noodlezip.search.dto.SearchFilterDto;
import noodlezip.search.dto.SearchStoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface StoreRepositoryCustom {

    Page<SearchStoreDto> findStoreOrderByDistance(double lat, double lng, Pageable pageable);

    Page<SearchStoreDto> searchStoresByFilter(SearchFilterDto filter, Pageable pageable);

    Page<RegistListDto> findWaitingStores(Pageable pageable);
}
