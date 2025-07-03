package noodlezip.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.util.PageUtil;
import noodlezip.community.dto.CommentDto;
import noodlezip.community.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PageUtil pageUtil;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findCommentList(Long boardId, Pageable pageable) {
        Page<CommentDto> commentDtoPage = commentRepository.findCommentByBoardIdWithUser(boardId, pageable);
        log.info("commentDtoPage: {}", commentDtoPage);
        Map<String, Object> map = pageUtil.getPageInfo(commentDtoPage, 10);
        map.put("comments", commentDtoPage.getContent());
        return map;
    }
}
