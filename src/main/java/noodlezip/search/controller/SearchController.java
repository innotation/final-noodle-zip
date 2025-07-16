package noodlezip.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.search.dto.SearchFilterDto;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.search.service.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/search")
@Controller
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/list")
    public String searchStore(@ModelAttribute SearchFilterDto filter, Model model){
        // 검색 파라미터를 모델에 추가
        if (filter.getKeyword() != null && !filter.getKeyword().trim().isEmpty()) {
            model.addAttribute("searchKeyword", filter.getKeyword());
        }
        if (filter.getSearchType() != null) {
            model.addAttribute("searchType", filter.getSearchType());
        }
        if (filter.getRegion() != null && !filter.getRegion().isEmpty()) {
            model.addAttribute("searchRegions", filter.getRegion());
        }
        if (filter.getRamenCategory() != null && !filter.getRamenCategory().isEmpty()) {
            model.addAttribute("searchCategories", filter.getRamenCategory());
        }
        if (filter.getRamenSoup() != null && !filter.getRamenSoup().isEmpty()) {
            model.addAttribute("searchSoups", filter.getRamenSoup());
        }
        if (filter.getTopping() != null && !filter.getTopping().isEmpty()) {
            model.addAttribute("searchToppings", filter.getTopping());
        }
        
        return "search/listing-map";
    }

    @GetMapping("/stores")
    @ResponseBody
    public Page<SearchStoreDto> getStoresByLocation(@RequestParam(required = false) Double lat,
                                                    @RequestParam(required = false) Double lng,
                                                    @RequestParam(defaultValue = "1") int page,
                                                    @RequestParam(defaultValue = "10") int size) {

        int pageIndex = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageIndex, size);

        if (lat == null || lng == null) {
            // 서울 시청 기준
            lat = 37.5665;
            lng = 126.9780;
        }

        return searchService.getPageLocation(lat, lng, pageable);
    }

    @GetMapping("/filter")
    @ResponseBody
    public Page<SearchStoreDto> searchStores(
            @ModelAttribute SearchFilterDto filter,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {

        int pageIndex = Math.max(page - 1, 0);
        Pageable pageable = PageRequest.of(pageIndex, size);

        if (filter.getLat() == null || filter.getLng() == null) {
            // 기본 위치 (서울 시청)
            filter.setLat(37.5665);
            filter.setLng(126.9780);
        }

        return searchService.searchStoresByFilter(filter, pageable);
    }

    @GetMapping("/filter-options")
    @ResponseBody
    public Map<String, Object> getFilterOptions() {
        return searchService.getFilterOptions();
    }

}
