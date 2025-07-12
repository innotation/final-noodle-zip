package noodlezip.mypage.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.mypage.dto.response.MyBoardListPageResponse;
import noodlezip.mypage.dto.response.MyCommentListPageResponse;
import noodlezip.mypage.service.MyCommunityService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
@Controller
@Tag(name = "마이페이지", description = "미이페이지 연동 API")
public class MyCommunityController extends MyBaseController {

    private final MyCommunityService myCommunityService;


    @Operation(
            summary = "사용자가 작성한 게시글 목록 조회",
            description = "사용자 마이페이지에서 해당 사용자가 작성한 게시글을 페이지 단위로 조회합니다."
    )
    @Parameters({
            @Parameter(name = "userId", description = "조회 대상 사용자 ID", required = true, example = "5"),
    })
    @GetMapping("/{userId}/boards")
    public String getMyPostBoards(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long userId,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        MyBoardListPageResponse boardList = myCommunityService.getMyPostBoardListPage(userId, pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        return "mypage/myBoard";
    }


    @Operation(
            summary = "사용자가 좋아요한 게시글 목록 조회",
            description = "사용자 마이페이지에서 해당 사용자가 좋아요한 게시글을 페이지 단위로 조회합니다."
    )
    @Parameters({
            @Parameter(name = "userId", description = "조회 대상 사용자 ID", required = true, example = "5"),
    })
    @GetMapping("/{userId}/liked-boards")
    public String getMyLikedBoards(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long userId,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        MyBoardListPageResponse boardList = myCommunityService.getMyLikedBoardListPage(userId, pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        return "mypage/myBoard";
    }


    @Operation(
            summary = "사용자가 작성한 댓글 목록 조회",
            description = "사용자 마이페이지에서 해당 사용자가 작성한 댓글을 페이지 단위로 조회합니다."
    )
    @Parameters({
            @Parameter(name = "userId", description = "조회 대상 사용자 ID", required = true, example = "5"),
    })
    @GetMapping("{userId}/comments")
    public String getMyComments(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long userId,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        MyCommentListPageResponse commentList = myCommunityService.getPostCommentListPage(userId, pageable);

        model.addAttribute("commentList", commentList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        return "mypage/myComment";
    }

}
