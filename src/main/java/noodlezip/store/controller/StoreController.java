package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.common.util.PageUtil;
import noodlezip.store.dto.*;
import noodlezip.store.service.StoreService;
import org.springframework.data.domain.Page;
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

    // 매장 상세페이지 진입
    @GetMapping("/detail.page")
    public String showDetailPage(Long no, Model model) {
        StoreDto store = storeService.getStore(no);
        model.addAttribute("store", store);
        return "store/detail";
    }

    // 매장 메뉴 조회 (비동기)
    @GetMapping("/detail/menuList")
    @ResponseBody
    public List<MenuDetailDto> showDetailMenuList(Long storeId) {
        return storeService.getMenus(storeId);
    }


    // 매장 리뷰 조회
    @GetMapping("/detail/review")
    @ResponseBody
    public StoreReviewResponseDto showStoreReviewList(
            @RequestParam Long storeId,
            Pageable pageable
    ) {

        Page<StoreReviewDto> reviews = storeService.getReviews(storeId, pageable);
        return StoreReviewResponseDto.builder()
                .reviews(reviews.getContent())
                .pagination(pageUtil.getPageInfo(reviews, 10))
                .build();
    }

}