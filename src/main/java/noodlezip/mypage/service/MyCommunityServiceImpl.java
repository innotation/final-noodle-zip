package noodlezip.mypage.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.CategoryCountDto;
import noodlezip.community.service.BoardService;
import noodlezip.community.service.CommentService;
import noodlezip.mypage.dto.response.MyBoardListPageResponse;
import noodlezip.mypage.dto.response.MyCommentListPageResponse;
import noodlezip.mypage.dto.response.MyCommentResponse;
import noodlezip.mypage.status.MyPageErrorStatus;
import noodlezip.user.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MyCommunityServiceImpl implements MyCommunityService {

    private final UserService userService;
    private final BoardService boardService;
    private final CommentService commentService;


    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public MyBoardListPageResponse getMyPostBoardListPage(Long userId, String communityType, Pageable pageable) {
        userService.findExistingUserByUserId(userId)
                .orElseThrow(() -> new CustomException(MyPageErrorStatus._NOT_FOUND_USER_MY_PAGE));

        List<CategoryCountDto> communityTypeList = boardService.getCategoryCountsByUser(userId);
        Map<String, Object> boardPageInfo = boardService.findBoardByUser(userId, communityType, pageable);

        MyBoardListPageResponse boardList = MyBoardListPageResponse.of(boardPageInfo);
        boardList.setBoardList((List<BoardRespDto>) boardPageInfo.get("list"));
        boardList.setCommunityTypeList(communityTypeList);

        return boardList;
    }


    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public MyBoardListPageResponse getMyLikedBoardListPage(Long userId, String communityType, Pageable pageable) {
        userService.findExistingUserByUserId(userId)
                .orElseThrow(() -> new CustomException(MyPageErrorStatus._NOT_FOUND_USER_MY_PAGE));

        List<Long> boardIdList = boardService.getBoardIdByUserLiked(userId);
        List<CategoryCountDto> communityTypeList = boardService.getCategoryCountsByBoardIds(boardIdList);
        Map<String, Object> boardPageInfo = boardService.findBoardLiked(userId, boardIdList, communityType, pageable);

        MyBoardListPageResponse boardList = MyBoardListPageResponse.of(boardPageInfo);
        boardList.setBoardList((List<BoardRespDto>) boardPageInfo.get("list"));
        boardList.setCommunityTypeList(communityTypeList);

        return boardList;
    }


    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public MyCommentListPageResponse getPostCommentListPage(Long userId, Pageable pageable) {
        userService.findExistingUserByUserId(userId)
                .orElseThrow(() -> new CustomException(MyPageErrorStatus._NOT_FOUND_USER_MY_PAGE));

        Map<String, Object> boardPageInfo = commentService.findCommentListByUserId(userId, pageable);
        MyCommentListPageResponse commentList = MyCommentListPageResponse.of(boardPageInfo);
        commentList.setCommentList((List<MyCommentResponse>) boardPageInfo.get("comments"));
        commentList.setTotalCommentCount((Long) boardPageInfo.get("totalComments"));

        return commentList;
    }

}
