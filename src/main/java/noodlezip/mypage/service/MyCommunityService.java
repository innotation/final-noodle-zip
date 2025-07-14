package noodlezip.mypage.service;

import noodlezip.mypage.dto.response.MyBoardListPageResponse;
import noodlezip.mypage.dto.response.MyCommentListPageResponse;
import org.springframework.data.domain.Pageable;

public interface MyCommunityService {

    MyBoardListPageResponse getMyPostBoardListPage(Long userId, Pageable pageable);

    MyBoardListPageResponse getMyLikedBoardListPage(Long userId, Pageable pageable);

    MyCommentListPageResponse getPostCommentListPage(Long userId, Pageable pageable);

}
