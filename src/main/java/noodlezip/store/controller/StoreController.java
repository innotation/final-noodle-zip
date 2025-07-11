package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.common.util.PageUtil;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.*;
import noodlezip.store.service.StoreService;
import noodlezip.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
public class StoreController {
    private final StoreService storeService;
    private final PageUtil pageUtil;
    private final RamenService ramenService;

    // 매장 상세페이지 진입
    @GetMapping("/detail/{no}")
    public String showDetailPage(@PathVariable Long no, Model model) {
        StoreDto store = storeService.getStore(no);
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
            return "store/fragments/tab-review :: moreReviews";  // 더보기 fragment
        }
    }

    // 메뉴 별 리뷰 평균조회
    @GetMapping("/detail/{storeId}/reviews/summary")
    @ResponseBody
    public ReviewSummaryDto getReviewSummaryByMenuName(
            @PathVariable Long storeId,
            @RequestParam String menuName
    ) {
        return ramenService.getSummaryByStoreIdAndMenuName(storeId, menuName);
    }

    // 전체 메뉴 리뷰 평균 조회
    @GetMapping("/detail/{storeId}/reviews/summary/all")
    @ResponseBody
    public ReviewSummaryDto getReviewSummaryAll(
            @PathVariable Long storeId
    ) {
        return ramenService.getSummaryByStoreId(storeId);
    }

    // 메뉴 토핑 조회
    @GetMapping("/detail/{storeId}/toppings")
    @ResponseBody
    public List<ToppingResponseDto> getToppingByStoreId(@PathVariable Long storeId){
        return storeService.getStoreToppings(storeId);
    }

}

