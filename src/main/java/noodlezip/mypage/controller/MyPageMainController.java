package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.user.entity.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class MyPageMainController {

    @GetMapping("/")
    public String myPage(@AuthenticationPrincipal MyUserDetails myUserDetails, Model model) { /// 자기 자신의 마이페이지로 이동
        User user = myUserDetails.getUser();

        model.addAttribute("userId", user.getId()); ///마이페이지 내부에서 하위 페이지 이동시 herf에 필요
//        return "index";
        throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{userId}")
    public String userMyPage(@PathVariable String userId, Model model) { /// 특정 사용자의 마이페이지로 이동
        return "index";
    }

}
