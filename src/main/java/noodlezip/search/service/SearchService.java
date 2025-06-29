package noodlezip.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.search.dto.SearchFilterDto;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SearchService {

    private final StoreRepository storeRepository;

    public Page<SearchStoreDto> getPageLocation(double lat, double lng, Pageable pageable){
        return storeRepository.findStoreOrderByDistance(lat, lng, pageable);
    }

    public Page<SearchStoreDto> searchStoresByFilter(SearchFilterDto filter, Pageable pageable) {
        return storeRepository.searchStoresByFilter(filter, pageable);
    }
}
