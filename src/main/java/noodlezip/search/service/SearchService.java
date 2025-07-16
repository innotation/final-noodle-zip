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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, Object> getFilterOptions() {
        Map<String, Object> options = new HashMap<>();
        
        // 카테고리 목록
        List<String> categories = storeRepository.findDistinctCategories();
        options.put("categories", categories);
        
        // 육수 목록
        List<String> soups = storeRepository.findDistinctSoups();
        options.put("soups", soups);
        
        // 토핑 목록
        List<String> toppings = storeRepository.findDistinctToppings();
        options.put("toppings", toppings);
        
        return options;
    }
}
