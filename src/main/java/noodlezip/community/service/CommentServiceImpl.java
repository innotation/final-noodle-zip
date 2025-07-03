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
    public void registComment(CommentReqDto commentReqDto) {
        Comment comment = Comment.builder()
                .communityId(commentReqDto.getBoardId())
                .user(userRepository.getReferenceById(commentReqDto.getUserId()))
                .commentStatus(CommunityActiveStatus.POSTED)
                .content(commentReqDto.getContent())
                .build();
        commentRepository.save(comment);
    }
}
