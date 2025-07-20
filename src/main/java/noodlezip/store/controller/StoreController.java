package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ApiResponse;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.PageUtil;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.RamenSoupResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.*;
import noodlezip.store.entity.Store;
import noodlezip.store.service.StoreService;
import noodlezip.store.status.StoreErrorCode;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
public class StoreController {

    private final StoreService storeService;
    private final PageUtil pageUtil;
    private final RamenService ramenService;

    // 매장 상세페이지 진입
    @GetMapping("/detail/{no}")
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

    // 매장 메뉴 조회 (비동기)
    @GetMapping("/detail/{no}/menuList")
    public String showDetailMenuList(@PathVariable Long no, Model model) {
        MenuDetailResponseDto menuDetail = storeService.getMenuDetail(no);
        model.addAttribute("menuDetail", menuDetail);
        return "store/fragments/tab-menu :: menu-tab";
    }

    // 매장 리뷰 조회
    @GetMapping("/detail/{no}/reviews")
    public String getReviewTab(@PathVariable Long no,
                               @RequestParam(defaultValue = "1") int page,
                               @RequestParam(required = false) String menuName,
                               Model model) {
        int size = 5;
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<StoreReviewDto> reviewPage = storeService.getReviews(no, pageable);

        if (page == 1) {
            if (menuName != null && !menuName.isEmpty()) {
                model.addAttribute("summary", ramenService.getSummaryByStoreIdAndMenuName(no, menuName));
            } else {
                model.addAttribute("summary", ramenService.getSummaryByStoreId(no));
            }
            model.addAttribute("reviewList", reviewPage.getContent());
            model.addAttribute("hasMore", reviewPage.hasNext());
            return "store/fragments/tab-review :: review-tab";
        } else {
            model.addAttribute("reviewList", reviewPage.getContent());
            model.addAttribute("hasMore", reviewPage.hasNext());
            model.addAttribute("nextPage", page + 1);
            return "store/fragments/tab-review :: moreReviews";  // 더보기 fragment
        }
    }

    // 메뉴 별 리뷰 평균조회
    @GetMapping("/detail/{storeId}/reviews/summary")
    @ResponseBody
    public ReviewSummaryDto getReviewSummaryByMenuName(@PathVariable Long storeId,
                                                       @RequestParam String menuName) {
        return ramenService.getSummaryByStoreIdAndMenuName(storeId, menuName);
    }

    // 전체 메뉴 리뷰 평균 조회
    @GetMapping("/detail/{storeId}/reviews/summary/all")
    @ResponseBody
    public ReviewSummaryDto getReviewSummaryAll(@PathVariable Long storeId) {
        return ramenService.getSummaryByStoreId(storeId);
    }

    // 메뉴 토핑 조회
    @GetMapping("/detail/{storeId}/toppings")
    @ResponseBody
    public List<ToppingResponseDto> getToppingByStoreId(@PathVariable Long storeId) {
        return storeService.getStoreToppings(storeId);
    }

    // 매장 삭제
    @DeleteMapping("/{storeId}")
    public ResponseEntity<?> deleteStore(@PathVariable Long storeId,
                                         @AuthenticationPrincipal MyUserDetails myUserDetails) {
        if (myUserDetails == null) {
            throw new CustomException(ErrorStatus._UNAUTHORIZED);
        }
        storeService.deleteStore(storeId, myUserDetails.getUser());
        return ResponseEntity.ok(Map.of("message", "삭제 완료"));
    }

    // 내가 등록한 매장 리스트
    @GetMapping("/my-list")
    public String myStoreList(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) {
        if (myUserDetails == null) {
            return "redirect:/"; // 메인 페이지로 리다이렉트
        }

        Long userId = myUserDetails.getUser().getId();
        List<Store> storeList = storeService.findStoresByUserId(userId);

        model.addAttribute("storeList", storeList);
        return "store/my-stores";
    }

    // 매장 수정
    @PostMapping("/update/{storeId}")
    public ResponseEntity<ApiResponse<Object>> updateStore(
            @PathVariable Long storeId,
            @RequestPart("dto") StoreRequestDto dto,
            @RequestPart(value = "storeMainImage", required = false) MultipartFile storeMainImage,
            @RequestPart(value = "existingStoreMainImageUrl", required = false) String existingStoreMainImageUrl,
            @RequestPart(value = "menuImageFiles", required = false) List<MultipartFile> menuImageFiles,
            @AuthenticationPrincipal MyUserDetails userDetails) {

        log.debug("처음 menuImage: {}", menuImageFiles);

        if (dto.getStoreMainImage() == null && existingStoreMainImageUrl != null) {
            dto.setStoreMainImageUrl(existingStoreMainImageUrl);
        }

        if (menuImageFiles != null) {
            for (int i = 0; i < dto.getMenus().size(); i++) {
                if (i < menuImageFiles.size()) {
                    dto.getMenus().get(i).setMenuImageFile(menuImageFiles.get(i));
                }
            }
        }

        log.debug("메뉴imagefiles dto에 넣은거: {} ", dto.getMenus().toString());

        storeService.updateStore(storeId, dto, storeMainImage, menuImageFiles, userDetails.getUser());

        return ApiResponse.onSuccess(StoreSuccessCode._SUCCESS_STORE_UPDATE);
    }

    // 매장 수정 페이지
    @GetMapping("/update/{storeId}")
    public String showUpdatePage(@PathVariable Long storeId,
                                 @AuthenticationPrincipal MyUserDetails userDetails,
                                 Model model) {
        if (userDetails == null) {
            // 인증 안 됐을 때 권한 없음을 의미하는 예외 던짐
            throw new CustomException(ErrorStatus._UNAUTHORIZED);
        }

        StoreRequestDto storeRequestDto = storeService.getStoreRequestDto(storeId, userDetails.getUser());
        List<CategoryResponseDto> categories = ramenService.getAllCategories();
        List<RamenSoupResponseDto> soups = ramenService.getAllSoups();

        model.addAttribute("storeRequestDto", storeRequestDto);
        model.addAttribute("categories", categories);
        model.addAttribute("soups", soups);
        model.addAttribute("toppings", ramenService.getAllToppings());

        return "store/update";
    }

    // 매장 단일 조회 (JSON 응답)
    @GetMapping("/{storeId}")
    @ResponseBody
    public ResponseEntity<StoreRequestDto> getStore(@PathVariable Long storeId,
                                                    @AuthenticationPrincipal MyUserDetails userDetails) {
        StoreRequestDto storeRequestDto = storeService.getStoreRequestDto(storeId, userDetails.getUser());
        return ResponseEntity.ok(storeRequestDto);
    }

    @ExceptionHandler(CustomException.class)
    public String handleCustomException(CustomException ex, RedirectAttributes redirectAttributes) {
        BaseErrorCode code = ex.getErrorCode();

        if (code == StoreErrorCode._FORBIDDEN || code == ErrorStatus._UNAUTHORIZED) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/";
        }
        if (code == StoreErrorCode._STORE_NOT_FOUND) {
            return "error/404";
        }
        return "error/500";
    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElement(NoSuchElementException ex) {
        return "error/404";
    }
}
