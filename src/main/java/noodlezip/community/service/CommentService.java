package noodlezip.community.service;

import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CommentService {

    Map<String, Object> findCommentList(Long boardId, Pageable pageable);
}
