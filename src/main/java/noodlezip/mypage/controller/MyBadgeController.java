package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.mypage.dto.request.BadgeDetailRequest;
import noodlezip.mypage.dto.response.BadgeDetailResponse;
import noodlezip.mypage.dto.response.MyBadgeBadgeResponse;
import noodlezip.mypage.service.MyBadgeService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/mypage")
@Controller
public class MyBadgeController {

    private final MyBadgeService myBadgeService;

    @GetMapping("/{userId}/badges/list")
    public String badge(@PathVariable Long userId, Model model) {
        List<MyBadgeBadgeResponse> userBadgeList = myBadgeService.getUserBadgeListByGroup(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("userBadgeList", userBadgeList);

        return "mypage/badge";
    }

    @PostMapping(value = "/badges/detail", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public BadgeDetailResponse getBadgeDetail(@RequestBody BadgeDetailRequest request) {
        return myBadgeService.getBadgeDetailList(
                request.getUserId(),
                request.getBadgeId(),
                request.getBadgeCategoryId()
        );
    }

}
