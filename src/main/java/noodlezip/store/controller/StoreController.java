package noodlezip.store.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.util.PageUtil;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.RamenSoupResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.*;
import noodlezip.store.service.StoreService;
import noodlezip.store.status.StoreSuccessCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
@Tag(name = "매장 관리", description = "매장 상세, 리뷰, 메뉴 등 매장 관련 API")
public class StoreController {
    private final StoreService storeService;

    private final RamenService ramenService;

    @GetMapping("/detail/{no}")
    @Operation(summary = "매장 상세페이지 진입", description = "매장 상세 정보를 조회하여 상세페이지를 반환합니다.")
    @Parameters({
        @Parameter(name = "no", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매장 상세페이지 반환 성공",
            content = @Content(mediaType = "text/html"))
    })
    public String showDetailPage(@PathVariable Long no,
                                 @AuthenticationPrincipal MyUserDetails myUserDetails,
                                 Model model) {
        Long userId = null;
        if (myUserDetails != null) {
            userId = myUserDetails.getUser().getId();
        }

        StoreDto store = storeService.getStore(no, userId);
        model.addAttribute("store", store);
        return "store/detail";
    }

    @GetMapping("/detail/{no}/menuList")
    @Operation(summary = "매장 메뉴 조회", description = "매장 ID로 메뉴 리스트를 비동기로 조회합니다.")
    @Parameters({
        @Parameter(name = "no", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매장 메뉴 fragment 반환 성공",
            content = @Content(mediaType = "text/html"))
    })
    public String showDetailMenuList(@PathVariable Long no, Model model) {

        MenuDetailResponseDto menuDetail = storeService.getMenuDetail(no);
        model.addAttribute("menuDetail", menuDetail);

        return "store/fragments/tab-menu :: menu-tab";
    }

    @GetMapping("/detail/{no}/reviews")
    @Operation(summary = "매장 리뷰 조회", description = "매장 ID로 리뷰 목록을 페이지네이션하여 조회합니다.")
    @Parameters({
        @Parameter(name = "no", description = "매장 ID", required = true, example = "1"),
        @Parameter(name = "page", description = "페이지 번호 (1부터 시작)", example = "1"),
        @Parameter(name = "menuName", description = "메뉴 이름(선택)", required = false)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매장 리뷰 fragment 반환 성공",
            content = @Content(mediaType = "text/html"))
    })
    public String getReviewTab(
            @PathVariable Long no,
            @RequestParam(defaultValue = "1") int page,  // 페이지는 1부터 시작한다고 가정
            @RequestParam(required = false) String menuName, // 메뉴 이름 추가
            Model model
    ) {
        int size = 5;  // 한 페이지당 리뷰 개수

        // Pageable은 0부터 페이지를 세니까 page-1로 변환
        Pageable pageable = PageRequest.of(page - 1, size);

        // 리뷰 페이징 조회
        Page<StoreReviewDto> reviewPage = storeService.getReviews(no, pageable);

        if (page == 1) {
            // 첫 페이지일 때는 리뷰 요약과 함께 전체 탭 내용을 로드
            if (menuName != null && !menuName.isEmpty()) {
                model.addAttribute("summary", ramenService.getSummaryByStoreIdAndMenuName(no, menuName));
            } else {
                model.addAttribute("summary", ramenService.getSummaryByStoreId(no));
            }
            model.addAttribute("reviewList", reviewPage.getContent());
            model.addAttribute("hasMore", reviewPage.hasNext());
            return "store/fragments/tab-review :: review-tab";  // 전체 탭 fragment
        } else {
            // 2페이지 이상부터는 리뷰 카드 목록만 더보기 fragment 로 반환
            model.addAttribute("reviewList", reviewPage.getContent());
            model.addAttribute("hasMore", reviewPage.hasNext());
            model.addAttribute("nextPage", page + 1);
            return "store/fragments/tab-review :: moreReviews";  // 더보기 fragment
        }
    }

    @GetMapping("/detail/{storeId}/reviews/summary")
    @Operation(summary = "메뉴별 리뷰 평균 조회", description = "매장 ID와 메뉴 이름으로 해당 메뉴의 리뷰 평균을 조회합니다.")
    @Parameters({
        @Parameter(name = "storeId", description = "매장 ID", required = true, example = "1"),
        @Parameter(name = "menuName", description = "메뉴 이름", required = true)
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "리뷰 평균 반환 성공",
            content = @Content(mediaType = "application/json"))
    })
    @ResponseBody
    public ReviewSummaryDto getReviewSummaryByMenuName(
            @PathVariable Long storeId,
            @RequestParam String menuName
    ) {
        return ramenService.getSummaryByStoreIdAndMenuName(storeId, menuName);
    }

    @GetMapping("/detail/{storeId}/reviews/summary/all")
    @Operation(summary = "전체 메뉴 리뷰 평균 조회", description = "매장 ID로 전체 메뉴의 리뷰 평균을 조회합니다.")
    @Parameters({
        @Parameter(name = "storeId", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "전체 리뷰 평균 반환 성공",
            content = @Content(mediaType = "application/json"))
    })
    @ResponseBody
    public ReviewSummaryDto getReviewSummaryAll(
            @PathVariable Long storeId
    ) {
        return ramenService.getSummaryByStoreId(storeId);
    }

    @GetMapping("/detail/{storeId}/toppings")
    @Operation(summary = "매장 토핑 조회", description = "매장 ID로 토핑 목록을 조회합니다.")
    @Parameters({
        @Parameter(name = "storeId", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "토핑 목록 반환 성공",
            content = @Content(mediaType = "application/json"))
    })
    @ResponseBody
    public List<ToppingResponseDto> getToppingByStoreId(@PathVariable Long storeId){
        return storeService.getStoreToppings(storeId);
    }

    @DeleteMapping("/{storeId}")
    @Operation(summary = "매장 삭제", description = "매장 ID로 매장을 삭제합니다. (로그인 필요)")
    @Parameters({
        @Parameter(name = "storeId", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 완료",
            content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId,
                                         @AuthenticationPrincipal MyUserDetails myUserDetails) {
        storeService.deleteStore(storeId, myUserDetails.getUser());
        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }

    @PostMapping("/update/{storeId}")
    @Operation(summary = "매장 수정", description = "매장 정보를 수정합니다. (로그인 필요)")
    @Parameters({
        @Parameter(name = "storeId", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매장 수정 성공",
            content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<?> updateStore(
            @PathVariable Long storeId,
            @RequestPart("dto") StoreRequestDto dto,
            @RequestPart(value = "storeMainImage", required = false) MultipartFile storeMainImage,
            @RequestPart(value = "existingStoreMainImageUrl", required = false) String existingStoreMainImageUrl,
            @RequestPart(value = "menuImageFiles", required = false) List<MultipartFile> menuImageFiles,
            @AuthenticationPrincipal MyUserDetails userDetails) {
        log.debug("처음 menuImage: {}", menuImageFiles);
        // 대표 이미지가 새로 업로드 되지 않은 경우 기존 URL 유지
        if (dto.getStoreMainImage() == null && existingStoreMainImageUrl != null) {
            dto.setStoreMainImageUrl(existingStoreMainImageUrl);
        }/* else {
            dto.setStoreMainImage(storeMainImage);
        }*/

        if (menuImageFiles != null) {
            for (int i = 0; i < dto.getMenus().size(); i++) {
                if (i < menuImageFiles.size()) {
                    dto.getMenus().get(i).setMenuImageFile(menuImageFiles.get(i));
                }
            }
        }
        log.debug("메뉴imagefiles dto에 넣은거: {} ", dto.getMenus().toString());
        storeService.updateStore(storeId, dto, storeMainImage, menuImageFiles, userDetails.getUser());

        return ResponseEntity.ok(noodlezip.common.dto.ApiResponse.onSuccess(StoreSuccessCode._SUCCESS_STORE_UPDATE));
    }

    @GetMapping("/update/{storeId}")
    @Operation(summary = "매장 수정 페이지", description = "매장 수정 폼 페이지를 반환합니다. (로그인 필요)")
    @Parameters({
        @Parameter(name = "storeId", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매장 수정 페이지 반환 성공",
            content = @Content(mediaType = "text/html"))
    })
    public String showUpdatePage(@PathVariable Long storeId,
                                 @AuthenticationPrincipal MyUserDetails userDetails,
                                 Model model) {
        // 매장 엔티티 → DTO로 변환
        StoreRequestDto storeRequestDto = storeService.getStoreRequestDto(storeId, userDetails.getUser());
        List<CategoryResponseDto> categories = ramenService.getAllCategories();
        List<RamenSoupResponseDto> soups = ramenService.getAllSoups();

        model.addAttribute("storeRequestDto", storeRequestDto);
        model.addAttribute("categories",categories);
        model.addAttribute("soups", soups);
        model.addAttribute("toppings", ramenService.getAllToppings());

        return "store/update";
    }

    @GetMapping("/{storeId}")
    @Operation(summary = "매장 상세 조회 (API)", description = "매장 ID로 매장 정보를 JSON으로 반환합니다.")
    @Parameters({
        @Parameter(name = "storeId", description = "매장 ID", required = true, example = "1")
    })
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "매장 정보 반환 성공",
            content = @Content(mediaType = "application/json"))
    })
    @ResponseBody
    public ResponseEntity<StoreRequestDto> getStore(@PathVariable Long storeId,
                                                    @AuthenticationPrincipal MyUserDetails userDetails) {
        StoreRequestDto storeRequestDto = storeService.getStoreRequestDto(storeId, userDetails.getUser());
        return ResponseEntity.ok(storeRequestDto);
    }
}