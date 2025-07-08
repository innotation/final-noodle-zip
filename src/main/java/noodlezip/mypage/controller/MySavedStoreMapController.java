package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.SavedStoreMapResponse;
import noodlezip.mypage.util.UserAccessInfo;
import noodlezip.savedstore.service.SavedStoreLocationService;
import noodlezip.savedstore.service.SavedStoreService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class MySavedStoreMapController extends MyBaseController {

    private final SavedStoreLocationService savedStoreLocationService;


    @GetMapping({
            "/my/saved-store/list/map",
            "/{userId}/saved-store/list/map"
    })
    @ResponseBody
    public SavedStoreMapResponse getMySavedStoreListMap(@AuthenticationPrincipal MyUserDetails userDetails,
                                                              @PathVariable(required = false) String userId,
                                                              @ModelAttribute SavedStoreCategoryFilterRequest filter
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(userDetails, userId);

        return savedStoreLocationService.getStoreLocationList(
                userAccessInfo.targetUserId(), filter, userAccessInfo.isOwner()
        );
    }

}
