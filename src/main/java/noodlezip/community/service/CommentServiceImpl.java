package noodlezip.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.util.PageUtil;
import noodlezip.community.dto.CommentReqDto;
import noodlezip.community.dto.CommentRespDto;
import noodlezip.community.entity.Comment;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.repository.CommentRepository;
import noodlezip.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findCommentList(Long boardId, Long userId, Pageable pageable) {
        Page<CommentRespDto> commentDtoPage = commentRepository.findCommentByBoardIdWithUser(boardId, userId, pageable);
        Map<String, Object> map = pageUtil.getPageInfo(commentDtoPage, 10);
        map.put("comments", commentDtoPage.getContent());
        map.put("totalComments", commentDtoPage.getTotalElements());
        return map;
    }

    @Override
    public Map<String, Object> registComment(CommentReqDto commentReqDto) {
        Comment comment = Comment.builder()
                .communityId(commentReqDto.getBoardId())
                .user(userRepository.getReferenceById(commentReqDto.getUserId()))
                .commentStatus(CommunityActiveStatus.POSTED)
                .content(commentReqDto.getContent())
                .build();
        commentRepository.save(comment);
        int pageSize = 10; // 한 페이지에 보여줄 댓글 수
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 생성일 기준으로 내림차순 정렬
        Pageable firstPage = PageRequest.of(0, pageSize, sort);
        Page<CommentRespDto> commentDtoPage = commentRepository.findCommentByBoardIdWithUser(commentReqDto.getBoardId(), commentReqDto.getUserId(), firstPage);
        Map<String, Object> map = pageUtil.getPageInfo(commentDtoPage, 10);
        map.put("comments", commentDtoPage.getContent());
        map.put("totalComments", commentDtoPage.getTotalElements());
        return map;
    }

    @Override
    public Map<String, Object> deleteComment(Long commentId, Long boardId, Long userId) {
        commentRepository.deleteById(commentId);
        int pageSize = 10; // 한 페이지에 보여줄 댓글 수
        Sort sort = Sort.by(Sort.Direction.DESC, "id"); // 생성일 기준으로 내림차순 정렬
        Pageable firstPage = PageRequest.of(0, pageSize, sort);
        Page<CommentRespDto> commentDtoPage = commentRepository.findCommentByBoardIdWithUser(boardId, userId, firstPage);
        Map<String, Object> map = pageUtil.getPageInfo(commentDtoPage, 10);
        map.put("comments", commentDtoPage.getContent());
        map.put("totalComments", commentDtoPage.getTotalElements());
        return map;
    }
}
