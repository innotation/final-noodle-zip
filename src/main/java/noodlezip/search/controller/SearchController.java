package noodlezip.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.search.service.SearchService;
import noodlezip.store.dto.StoreDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/search")
@Controller
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/list")
    public String searchStore(){
        return "/search/listing-map";
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

}
