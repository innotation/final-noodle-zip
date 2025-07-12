package noodlezip.subscription.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.controller.MyBaseController;
import noodlezip.mypage.dto.UserAccessInfo;
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
public class MyUserSubscribeController extends MyBaseController {

    private final SubscribeService subscribeService;


    @Operation(
            summary = "사용자 구독 목록 조회",
            description = "특정 사용자의 구독 목록을 조회합니다. 기본 이동 페이지는 follower 페이지입니다."
    )
    @Tag(name = "마이페이지", description = "미이페이지 연동 API")
    @Parameters({
            @Parameter(name = "userId", description = "조회될 사용자 PK"),
            @Parameter(name = "myUserDetails", hidden = true),
            @Parameter(name = "model", hidden = true)
    })
    @GetMapping("/{userId}/follower")
    public String getFollowers(@AuthenticationPrincipal MyUserDetails myUserDetails,
                               @PathVariable Long userId,
                               @RequestParam(defaultValue = SubscriptionPagePolicy.DEFAULT_PAGE) int page,
                               Model model
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        SubscriptionPageResponse followerList = subscribeService.getFollowerListWithPaging(
                userId, userAccessInfo.getRequestUserId(), page);

        model.addAttribute("list", followerList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        return "mypage/subscription";
    }


    @GetMapping("/{userId}/following")
    public String getFollowing(@AuthenticationPrincipal MyUserDetails myUserDetails,
                               @PathVariable Long userId,
                               @RequestParam(defaultValue = SubscriptionPagePolicy.DEFAULT_PAGE) int page,
                               Model model
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        SubscriptionPageResponse followingList = subscribeService.getFollowingListWithPaging(
                userId, userAccessInfo.getRequestUserId(), page);

        model.addAttribute("list", followingList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        return "mypage/subscription";
    }

}
