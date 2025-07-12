package noodlezip.mypage.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.CommentRespDto;
import noodlezip.mypage.dto.UserAccessInfo;
import noodlezip.mypage.dto.response.MyBoardListPageResponse;
import noodlezip.mypage.dto.response.MyCommentListPageResponse;
import noodlezip.mypage.dto.response.MyCommentResponse;
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
public class MyCommunityController extends MyBaseController {

    private final MyCommunityService myCommunityService;


    @GetMapping("/{userId}/boards")
    public String getMyPostBoards(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long userId,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        MyBoardListPageResponse boardList = myCommunityService.getMyPostBoardListPage(userId,pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        for(BoardRespDto dto : boardList.getBoardList()) {
            System.out.println(dto);
        }
        return "mypage/myBoard";
    }


    @GetMapping("/{userId}/liked-boards")
    public String getMyLikedBoards(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long userId,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        MyBoardListPageResponse boardList = myCommunityService.getMyLikedBoardListPage(userId,pageable);

        model.addAttribute("boardList", boardList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        for(BoardRespDto dto : boardList.getBoardList()) {
            System.out.println(dto);
        }

        return "mypage/myBoard";
    }


    @GetMapping("{userId}/comments")
    public String getMyComments(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @PathVariable Long userId,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model
    ) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        UserAccessInfo userAccessInfo = resolveUserAccess(myUserDetails, userId);
        MyCommentListPageResponse commentList = myCommunityService.getPostCommentListPage(userId,pageable);

        model.addAttribute("commentList", commentList);
        model.addAttribute("userAccessInfo", userAccessInfo);

        for(MyCommentResponse dto : commentList.getCommentList()) {
            System.out.println(dto);
        }
        return "mypage/myComment";
    }

}
