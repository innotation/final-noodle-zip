package noodlezip.savedstore.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.controller.MyBaseController;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreMapResponse;
import noodlezip.savedstore.service.SavedStoreLocationService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class MySavedStoreMapController extends MyBaseController {

    private final SavedStoreLocationService savedStoreLocationService;


    @GetMapping("/{userId}/saved-stores/map")
    @ResponseBody
    public SavedStoreMapResponse getMySavedStoreListMap(@AuthenticationPrincipal MyUserDetails userDetails,
                                                        @PathVariable Long userId,
                                                        @ModelAttribute SavedStoreCategoryFilterRequest filter
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(userDetails, userId);

        return savedStoreLocationService.getStoreLocationList(
                userAccessInfo.getTargetUserId(), filter, userAccessInfo.getIsOwner()
        );
    }

}
