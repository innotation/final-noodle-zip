package noodlezip.savedstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.controller.MyBaseController;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.MySavedStorePageResponse;
import noodlezip.savedstore.dto.response.SavedStoreListWithPageInfoResponse;
import noodlezip.savedstore.dto.response.SavedStorePageResponse;
import noodlezip.savedstore.service.MySavedStoreService;
import noodlezip.savedstore.service.SavedStoreService;
import noodlezip.savedstore.util.SavedStorePagePolicy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class MySavedStoreController extends MyBaseController {

    private final MySavedStoreService mySavedStoreService;
    private final SavedStoreService savedStoreService;


    @Operation(
            summary = "사용자 저장 가게 목록 조회",
            description = "특정 사용자의 저장 가게 목록을 조회합니다."
    )
    @Tag(name = "마이페이지", description = "미이페이지 연동 API")
    @Parameters({
            @Parameter(name = "userId", description = "조회될 사용자 PK"),
            @Parameter(name = "myUserDetails", hidden = true),
            @Parameter(name = "model", hidden = true)
    })
    @GetMapping("/{userId}/saved-stores")
    public String savedStoreList(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                 @PathVariable Long userId,
                                 @ModelAttribute SavedStoreCategoryFilterRequest filter,
                                 Model model
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        Long targetUserId = userAccessInfo.getTargetUserId();
        boolean isOwner = userAccessInfo.getIsOwner();

        if (isOwner) {
            MySavedStorePageResponse mySavedStorePageInfo = mySavedStoreService.getMySavedStoreInitPage(targetUserId, filter);
            model.addAttribute("mySavedStorePageInfo", mySavedStorePageInfo);
        } else {
            SavedStorePageResponse savedStorePageInfo = mySavedStoreService.getSavedStoreInitPage(targetUserId, filter);
            model.addAttribute("savedStorePageInfo", savedStorePageInfo);
        }
        model.addAttribute("userAccessInfo", userAccessInfo);

        return "mypage/savedStore";
    }


    @GetMapping("/{userId}/saved-stores/category-filter-search")
    @ResponseBody
    public SavedStoreListWithPageInfoResponse getMySavedStoreListByCategory(
            @AuthenticationPrincipal MyUserDetails userDetails,
            @PathVariable Long userId,
            @ModelAttribute SavedStoreCategoryFilterRequest filter,
            @RequestParam(defaultValue = SavedStorePagePolicy.DEFAULT_PAGE) int page
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(userDetails, userId);

        return savedStoreService.getSavedStoreListWithPaging(
                userAccessInfo.getTargetUserId(), filter, page, userAccessInfo.getIsOwner()
        );
    }

}

