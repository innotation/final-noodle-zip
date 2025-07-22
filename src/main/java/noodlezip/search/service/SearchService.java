package noodlezip.search.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.search.dto.SearchFilterDto;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.repository.StoreRepository;
import org.springframework.dao.DataAccessException;
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
        if (pageable == null) {
            throw new IllegalArgumentException("pageable이 null입니다.");
        }
        if (lat < -90 || lat > 90 || lng < -180 || lng > 180) {
            throw new IllegalArgumentException("잘못된 위도, 경도 범위입니다.");
        }
        try {
            return storeRepository.findStoreOrderByDistance(lat, lng, pageable);
        } catch (DataAccessException e) {
            log.error("DB 오류(getPageLocation): {}", e.getMessage(), e);
            throw e;
        }
    }

    public Page<SearchStoreDto> searchStoresByFilter(SearchFilterDto filter, Pageable pageable) {
        if (filter == null) {
            throw new IllegalArgumentException("필터값이 없습니다.");
        }
        if (pageable == null) {
            throw new IllegalArgumentException("pageable이 null입니다.");
        }
        try {
            return storeRepository.searchStoresByFilter(filter, pageable);
        } catch (DataAccessException e) {
            log.error("DB 오류(searchStoresByFilter): {}", e.getMessage(), e);
            throw e;
        }
    }

    public Map<String, Object> getFilterOptions() {
        Map<String, Object> options = new HashMap<>();
        try {
            // 카테고리 목록
            List<String> categories = storeRepository.findDistinctCategories();
            options.put("categories", categories);
            // 육수 목록
            List<String> soups = storeRepository.findDistinctSoups();
            options.put("soups", soups);
            // 토핑 목록
            List<String> toppings = storeRepository.findDistinctToppings();
            options.put("toppings", toppings);
        } catch (DataAccessException e) {
            log.error("DB 오류(getFilterOptions): {}", e.getMessage(), e);
            throw e;
        }
        return options;
    }
}
