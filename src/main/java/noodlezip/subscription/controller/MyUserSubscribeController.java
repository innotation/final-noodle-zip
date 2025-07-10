package noodlezip.subscription.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.subscription.dto.response.FollowerPageResponse;
import noodlezip.subscription.dto.response.FollowingPageResponse;
import noodlezip.subscription.service.SubscribeService;
import noodlezip.subscription.util.SubscriptionPagePolicy;
import noodlezip.user.entity.User;
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


    @GetMapping("/{userId}/followers")
    public String getFollowers(@AuthenticationPrincipal MyUserDetails myUserDetails,
                               @PathVariable Long userId,
                               @RequestParam(defaultValue = SubscriptionPagePolicy.DEFAULT_PAGE) int page,
                               Model model
    ) {
        User user = myUserDetails.getUser();
        Long targetUserId = user.getId();
        FollowerPageResponse followerList = subscribeService.getFollowerListWithPaging(userId, targetUserId, page);

        model.addAttribute("followerList", followerList);

        return "index";
    }

    @GetMapping("/{userId}/following")
    public String getFollowing(@AuthenticationPrincipal MyUserDetails myUserDetails,
                               @PathVariable Long userId,
                               @RequestParam(defaultValue = SubscriptionPagePolicy.DEFAULT_PAGE) int page,
                               Model model
    ) {
        User user = myUserDetails.getUser();
        Long targetUserId = user.getId();
        FollowingPageResponse followingList = subscribeService.getFollowingListWithPaging(userId, targetUserId, page);

        model.addAttribute("followingList", followingList);

        return "index";
    }

}
