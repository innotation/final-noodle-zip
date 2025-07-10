package noodlezip.subscription.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.subscription.service.SubscribeService;
import noodlezip.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class UserSubscribeController {

    private final SubscribeService subscribeService;


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @GetMapping("{userId}/subscribe")
    public void subscribe(@AuthenticationPrincipal MyUserDetails myUserDetails, @PathVariable Long userId) {
        User user = myUserDetails.getUser();
        Long targetUserId = user.getId();

        subscribeService.handleSubscribe(targetUserId, userId);
    }

}
