package noodlezip.community.repository;

import noodlezip.community.dto.CommentRespDto;
import noodlezip.mypage.dto.response.MyCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<CommentRespDto> findCommentByBoardIdWithUser(Long boardId, Long userId,Pageable pageable);
    Page<MyCommentResponse> findCommentByUserId(Long userId, Pageable pageable);
}
