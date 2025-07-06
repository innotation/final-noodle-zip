package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.MySavedStorePageResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStoreListResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStorePageResponse;
import noodlezip.mypage.dto.response.savedstore.StoreLocationResponse;
import noodlezip.mypage.service.MySavedStoreService;
import noodlezip.mypage.util.SavedStorePagePolicy;
import noodlezip.savedstore.service.SavedStoreService;
import noodlezip.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class MySavedStoreController {

    private final MySavedStoreService mySavedStoreService;
    private final SavedStoreService savedStoreService;

    @GetMapping("/my/saved-store/list")
    public String mySavedStoreList(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        MySavedStorePageResponse mySavedStorePageInfo = mySavedStoreService.getMySavedStoreInitPage(user.getId());

        model.addAttribute("userId", user.getId());
        model.addAttribute("isOwner", true);
        model.addAttribute("mySavedStorePageInfo", mySavedStorePageInfo);

        return "index";
    }

    @GetMapping("/{userId}/saved-store/list")
    public String savedStoreList(@PathVariable Long userId, Model model) {
        SavedStorePageResponse savedStorePageInfo = mySavedStoreService.getSavedStoreInitPage(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("isOwner", false);
        model.addAttribute("savedStorePageInfo", savedStorePageInfo);

        return "index";
    }


    /// 카테고리 조회 + 페이지네이션
    @GetMapping("/{userId}/saved-store/list/category-filter-search")
    @ResponseBody
    public SavedStoreListResponse getMySavedStoreListByCategory(@AuthenticationPrincipal MyUserDetails userDetails,
                                                                @PathVariable Long userId,
                                                                @ModelAttribute SavedStoreCategoryFilterRequest filter,
                                                                @RequestParam(defaultValue =
                                                                        SavedStorePagePolicy.DEFAULT_PAGE) int page
    ) {
        User user = userDetails.getUser();
        boolean isOwner = user.getId().equals(userId);

        return savedStoreService.getSavedStoreListWithPaging(
                userId, filter, page, isOwner
        );
    }


    /// 사용자가 지도보기를 눌렀을때  storeId+좌표 리스트 비동기 조회

    /**
     * 선택된 카테고리 전체?
     * 현재 페이지네이션 기준? : 그럼 List<sotreId>로 받아야됨
     */
    @GetMapping("/{userId}/saved-store/list/map")
    @ResponseBody
    public List<StoreLocationResponse> getMySavedStoreListMap(@AuthenticationPrincipal MyUserDetails userDetails,
                                                              @PathVariable Long userId,
                                                              @ModelAttribute SavedStoreCategoryFilterRequest filter
    ) {
        User user = userDetails.getUser();
        boolean isOwner = user.getId().equals(userId);

        return savedStoreService.getStoreLocationList(
                userId, filter, isOwner
        );
    }

}

