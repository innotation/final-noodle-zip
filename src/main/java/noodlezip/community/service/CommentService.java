package noodlezip.community.service;

import noodlezip.community.dto.CommentReqDto;
import noodlezip.community.dto.CommentRespDto;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface CommentService {

    Map<String, Object> findCommentList(Long boardId, Pageable pageable);

    void registComment(CommentReqDto commentReqDto);
}
