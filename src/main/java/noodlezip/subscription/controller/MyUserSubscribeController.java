package noodlezip.subscription.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.subscription.dto.response.SubscriptionPageResponse;
import noodlezip.subscription.service.SubscribeService;
import noodlezip.subscription.util.SubscriptionPagePolicy;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class MyUserSubscribeController {

    private final SubscribeService subscribeService;


    @GetMapping("/{userId}/follower")
    public String getFollowers(@AuthenticationPrincipal MyUserDetails myUserDetails,
                               @PathVariable Long userId,
                               @RequestParam(defaultValue = SubscriptionPagePolicy.DEFAULT_PAGE) int page,
                               Model model
    ) {
        Long requestUserId = (myUserDetails != null)
                ? myUserDetails.getUser().getId()
                : null;
        SubscriptionPageResponse followerList = subscribeService.getFollowerListWithPaging(userId, requestUserId, page);

        model.addAttribute("list", followerList);

        return "mypage/subscription";
    }


    @GetMapping("/{userId}/following")
    public String getFollowing(@AuthenticationPrincipal MyUserDetails myUserDetails,
                               @PathVariable Long userId,
                               @RequestParam(defaultValue = SubscriptionPagePolicy.DEFAULT_PAGE) int page,
                               Model model
    ) {
        Long requestUserId = (myUserDetails != null)
                ? myUserDetails.getUser().getId()
                : null;
        SubscriptionPageResponse followingList = subscribeService.getFollowingListWithPaging(userId, requestUserId, page);

        model.addAttribute("list", followingList);

        return "mypage/subscription";
    }

}
