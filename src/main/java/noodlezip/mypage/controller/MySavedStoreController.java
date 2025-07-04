package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class MySavedStoreController {

    @GetMapping("/my/saved-store/list")
    /// 사용자가 본인의 마이페이지에 접근해야만 들어올 수 있음
    public String mySavedStoreList(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {

        model.addAttribute("isOwner", true);
        return "index";
    }

    @GetMapping("/{userId}/saved-store/list")
    public String savedStoreList(@AuthenticationPrincipal MyUserDetails userDetails, Model model) {

        model.addAttribute("isOwner", false);
        return "index";
    }


}
