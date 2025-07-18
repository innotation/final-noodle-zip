package noodlezip.badge.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.badge.dto.response.BadgeDetailResponse;
import noodlezip.badge.dto.response.MyBadgeBadgeResponse;
import noodlezip.badge.service.MyBadgeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
public class MyBadgeController {

    private final MyBadgeService myBadgeService;


    @Operation(
            summary = "사용자 배지 목록 조회",
            description = "특정 사용자의 달성 배지 목록을 조회합니다."
    )
    @Tag(name = "마이페이지", description = "미이페이지 연동 API")
    @Parameters({
            @Parameter(name = "userId", description = "조회될 사용자 PK"),
            @Parameter(name = "myUserDetails", hidden = true),
            @Parameter(name = "model", hidden = true)
    })
    @GetMapping("/{userId}/badges")
    public String badge(@PathVariable Long userId, Model model) {
        List<MyBadgeBadgeResponse> userBadgeList = myBadgeService.getUserBadgeListByGroup(userId);

        model.addAttribute("userId", userId);
        model.addAttribute("userBadgeList", userBadgeList);

        return "mypage/badge";
    }


    @GetMapping(value = "/badges/{badgeId}")
    @ResponseBody
    public BadgeDetailResponse getBadgeDetail(@PathVariable Long badgeId,
                                              @RequestParam Long userId,
                                              @RequestParam Long badgeCategoryId
    ) {
        return myBadgeService.getBadgeDetailList(userId, badgeId, badgeCategoryId );
    }

}
