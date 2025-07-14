package noodlezip.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.dto.UserAccessInfo;
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
@Tag(name = "마이페이지", description = "미이페이지 연동 API")
public class MyPageMainController extends MyBaseController {

    @Operation(
            summary = "마이페이지 메인페이지 정보 조회",
            description = "특정 사용자의 마이페이지 정보를 조회합니다."
    )
    @Parameters({
            @Parameter(name = "userId", description = "조회될 사용자 PK"),
            @Parameter(name = "myUserDetails", hidden = true),
            @Parameter(name = "model", hidden = true)
    })
    @GetMapping("/{userId}")
    public String userMyPage(@AuthenticationPrincipal MyUserDetails myUserDetails,
                             @PathVariable Long userId,
                             Model model
    ) {
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);

        model.addAttribute("userAccessInfo", userAccessInfo);

        return "mypage/main";
    }

    /**
     * 마이페이지 메인 페이지        /mypage/{userId}
     * 배지 조회                  /users/{userId}/badges
     * 저장가게 조회               /users/{userId}/saved-stores
     * 구독 목록 조회              /users/{userId}/follower
     * 내가 쓴 게시글 조회 :        /users/{userId}/boards
     * 내가 좋아요한 게시글 조회 :    /users/{userId}/liked-boards
     * 내가 단 댓글 조회 :          /users/{userId}/comments
     */

}
