package noodlezip.search.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "검색 관리", description = "검색 관련 API")
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/list")
    @Operation(summary = "검색 페이지", description = "검색 조건에 따라 매장 리스트 페이지를 반환합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 페이지 반환 성공",
            content = @Content(mediaType = "text/html"))
    })
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
        if (filter.getDistance() != null) {
            model.addAttribute("searchDistance", filter.getDistance());
        }
        if (filter.getSort() != null) {
            model.addAttribute("searchSort", filter.getSort());
        }
        
        return "search/listing-map";
    }

    @GetMapping("/stores")
    @Operation(summary = "위치 기반 매장 목록 조회", description = "위도, 경도 기준으로 매장 목록을 페이지네이션하여 조회합니다.")
    @Parameters({
        @Parameter(name = "lat", description = "위도", example = "37.5665"),
        @Parameter(name = "lng", description = "경도", example = "126.9780"),
        @Parameter(name = "page", description = "페이지 번호", example = "1"),
        @Parameter(name = "size", description = "페이지 크기", example = "10")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매장 목록 반환 성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SearchStoreDto.class)))
    })
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
    @Operation(summary = "필터 검색", description = "필터 조건에 따라 매장 목록을 조회합니다.")
    @Parameters({
        @Parameter(name = "lat", description = "위도", example = "37.5665"),
        @Parameter(name = "lng", description = "경도", example = "126.9780"),
        @Parameter(name = "page", description = "페이지 번호", example = "1"),
        @Parameter(name = "size", description = "페이지 크기", example = "10")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 결과 반환 성공",
            content = @Content(mediaType = "application/json",
                schema = @Schema(implementation = SearchStoreDto.class)))
    })
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
    @Operation(summary = "필터 옵션 조회", description = "검색 필터에 사용되는 옵션(카테고리, 육수, 토핑 등)을 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "필터 옵션 반환 성공",
            content = @Content(mediaType = "application/json"))
    })
    @ResponseBody
    public Map<String, Object> getFilterOptions() {
        return searchService.getFilterOptions();
    }

}
