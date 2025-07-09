package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.dto.MyPageAuthorityContext;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.MySavedStorePageResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStoreListWithPageInfoResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStorePageResponse;
import noodlezip.mypage.dto.response.savedstore.StoreLocationResponse;
import noodlezip.mypage.service.MySavedStoreService;
import noodlezip.mypage.util.MyPageUrlPolicy;
import noodlezip.mypage.util.SavedStorePagePolicy;
import noodlezip.mypage.util.UserAccessInfo;
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
public class MySavedStoreController extends MyBaseController { /// todo savedStore 도메인으로 이동

    private final MySavedStoreService mySavedStoreService;
    private final SavedStoreService savedStoreService;


    @GetMapping("/my/saved-store/list")
    public String mySavedStoreList(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {
        User user = userDetails.getUser();
        MySavedStorePageResponse mySavedStorePageInfo = mySavedStoreService.getMySavedStoreInitPage(user.getId());
        MyPageAuthorityContext authorityContext = new MyPageAuthorityContext(user.getId(), true);

        model.addAttribute("authorityContext", authorityContext);
        model.addAttribute("mySavedStorePageInfo", mySavedStorePageInfo);

        return "mypage/savedStore";
    }


    @GetMapping("/{userId}/saved-store/list")
    public String savedStoreList(@PathVariable Long userId, Model model) {
        SavedStorePageResponse savedStorePageInfo = mySavedStoreService.getSavedStoreInitPage(userId);
        MyPageAuthorityContext authorityContext = new MyPageAuthorityContext(userId, false);

        model.addAttribute("authorityContext", authorityContext);
        model.addAttribute("savedStorePageInfo", savedStorePageInfo);

        return "mypage/savedStore";
    }


    @GetMapping({
            "/my/saved-store/list/category-filter-search",
            "/{userId}/saved-store/list/category-filter-search"
    })
    @ResponseBody
    public SavedStoreListWithPageInfoResponse getMySavedStoreListByCategory(@AuthenticationPrincipal MyUserDetails userDetails,
                                                                            @PathVariable(required = false) String userId,
                                                                            @ModelAttribute SavedStoreCategoryFilterRequest filter,
                                                                            @RequestParam(defaultValue =
                                                                                    SavedStorePagePolicy.DEFAULT_PAGE) int page
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(userDetails, userId);

        return savedStoreService.getSavedStoreListWithPaging(
                userAccessInfo.targetUserId(), filter, page, userAccessInfo.isOwner()
        );
    }


    @GetMapping({
            "/my/saved-store/list/map",
            "/{userId}/saved-store/list/map"
    })
    @ResponseBody
    public List<StoreLocationResponse> getMySavedStoreListMap(@AuthenticationPrincipal MyUserDetails userDetails,
                                                              @PathVariable(required = false) String userId,
                                                              @ModelAttribute SavedStoreCategoryFilterRequest filter
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(userDetails, userId);

        return savedStoreService.getStoreLocationList(
                userAccessInfo.targetUserId(), filter, userAccessInfo.isOwner()
        );
    }

}

