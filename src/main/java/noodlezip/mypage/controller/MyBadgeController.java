package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.service.MyBadgeService;
import noodlezip.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class MyBadgeController {

    private final MyBadgeService myBadgeService;


    @GetMapping("/my/badge")
    public String badge(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) { /// 본인지
        User user = myUserDetails.getUser();


        model.addAttribute("userName", user.getUserName());
        /// 배지 결과
        return "badge";
    }

    @GetMapping("{userId}/badge")
    public String badge(@PathVariable Long userId, Model model) { /// 다른사람
        return "badge";
    }

    /// 동적으로 배지 상세 페이지 정보 보내기(user_id + badge_category_id)

    /// 동적으로 즐겨찾기 및 즐겨찾기 가능 여부 보내기  +  message, alert()

    /// 동적으로 배지 숨김 처리 하기  +  message, alert()
}
