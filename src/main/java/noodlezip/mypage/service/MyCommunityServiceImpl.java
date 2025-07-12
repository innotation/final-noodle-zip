package noodlezip.mypage.service;

import lombok.RequiredArgsConstructor;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.CommentRespDto;
import noodlezip.community.service.BoardService;
import noodlezip.community.service.CommentService;
import noodlezip.mypage.dto.response.MyBoardListPageResponse;
import noodlezip.mypage.dto.response.MyCommentListPageResponse;
import noodlezip.mypage.dto.response.MyCommentResponse;
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
    public MyBoardListPageResponse getMyPostBoardListPage(Long userId, Pageable pageable) {
        userService.validateMyPageExistingUserByUserId(userId);

        Map<String, Object> boardPageInfo = boardService.findBoardByUser(userId, pageable);
        MyBoardListPageResponse boardList = MyBoardListPageResponse.of(boardPageInfo);
        boardList.setBoardList((List<BoardRespDto>) boardPageInfo.get("list"));

        return boardList;
    }

    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public MyBoardListPageResponse getMyLikedBoardListPage(Long userId, Pageable pageable) {
        userService.validateMyPageExistingUserByUserId(userId);

        Map<String, Object> boardPageInfo = boardService.findBoardLiked(userId, pageable);
        MyBoardListPageResponse boardList = MyBoardListPageResponse.of(boardPageInfo);
        boardList.setBoardList((List<BoardRespDto>) boardPageInfo.get("list"));

        return boardList;
    }


    @SuppressWarnings("unchecked")
    @Override
    @Transactional(readOnly = true)
    public MyCommentListPageResponse getPostCommentListPage(Long userId, Pageable pageable) {
        userService.validateMyPageExistingUserByUserId(userId);

        Map<String, Object> boardPageInfo = commentService.findCommentListByUserId(userId, pageable);
        MyCommentListPageResponse commentList = MyCommentListPageResponse.of(boardPageInfo);
        commentList.setCommentList((List<MyCommentResponse>) boardPageInfo.get("comments"));
        commentList.setTotalCommentCount((Long) boardPageInfo.get("totalComments"));

        return commentList;
    }

}
