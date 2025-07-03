package noodlezip.community.repository;

import noodlezip.community.dto.CommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<CommentDto> findCommentByBoardIdWithUser(Long boardId, Pageable pageable);
}
