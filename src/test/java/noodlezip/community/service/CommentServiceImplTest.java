package noodlezip.community.service;

import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.PageUtil;
import noodlezip.community.dto.CommentReqDto;
import noodlezip.community.dto.CommentRespDto;
import noodlezip.community.entity.Comment;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.repository.BoardRepository;
import noodlezip.community.repository.CommentRepository;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class CommentServiceImplTest {

    @InjectMocks private CommentServiceImpl commentService;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PageUtil pageUtil;

    @Mock
    private BoardRepository boardRepository;

    private User testUser;
    private CommentReqDto testCommentReqDto;
    private CommentRespDto testCommentRespDto;
    private Comment testComment;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .userName("testuser")
                .build();

        testCommentReqDto = CommentReqDto.builder()
                .boardId(10L)
                .userId(1L)
                .content("새 댓글 내용")
                .build();

        testCommentRespDto = CommentRespDto.builder()
                .id(10L)
                .userId(1L)
                .content("테스트 댓글 내용")
                .build();

        testComment = Comment.builder()
                .id(1L)
                .communityId(10L)
                .user(testUser)
                .commentStatus(CommunityActiveStatus.POSTED)
                .content("테스트 댓글 내용")
                .build();
    }

    @Test
    @DisplayName("댓글 목록 조회 성공")
    void findCommentList_Success() {
        // Given
        Long boardId = 10L;
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<CommentRespDto> commentRespList = Arrays.asList(
                testCommentRespDto,
                CommentRespDto.builder().id(10L).userId(1L).content("댓글2").build()
        );
        Page<CommentRespDto> commentPage = new PageImpl<>(commentRespList, pageable, 2);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("totalPage", 1);
        pageInfoMap.put("currentPage", 1);
        pageInfoMap.put("beginPage", 1);
        pageInfoMap.put("endPage", 1);

        // Mocking behavior
        when(commentRepository.findCommentByBoardIdWithUser(anyLong(), anyLong(), any(Pageable.class)))
                .thenReturn(commentPage);
        when(pageUtil.getPageInfo(any(Page.class), anyInt())).thenReturn(pageInfoMap);

        // When
        Map<String, Object> result = commentService.findCommentList(boardId, userId, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsKey("comments");
        assertThat(result.get("comments")).isEqualTo(commentRespList);
        assertThat(result).containsKey("totalComments");
        assertThat(result.get("totalComments")).isEqualTo(2L);

        verify(commentRepository, times(1)).findCommentByBoardIdWithUser(boardId, userId, pageable);
        verify(pageUtil, times(1)).getPageInfo(commentPage, 10);
    }

    @Test
    @DisplayName("댓글 등록 성공")
    void registComment_Success() {
        // Given
        Long boardId = testCommentReqDto.getBoardId();
        Long userId = testCommentReqDto.getUserId();
        int pageSize = 10;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable firstPage = PageRequest.of(0, pageSize, sort);

        List<CommentRespDto> updatedCommentList = Arrays.asList(
                CommentRespDto.builder().id(boardId).userId(userId).content("새 댓글 내용").build(),
                testCommentRespDto
        );
        Page<CommentRespDto> updatedCommentPage = new PageImpl<>(updatedCommentList, firstPage, 2);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("totalPage", 1);
        pageInfoMap.put("currentPage", 1);
        pageInfoMap.put("beginPage", 1);
        pageInfoMap.put("endPage", 1);

        // Mocking behavior
        when(boardRepository.existsById(boardId)).thenReturn(true); // 게시글 존재
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment); // save 호출 시 testComment 반환
        when(commentRepository.findCommentByBoardIdWithUser(boardId, userId, firstPage))
                .thenReturn(updatedCommentPage);
        when(pageUtil.getPageInfo(any(Page.class), anyInt())).thenReturn(pageInfoMap);

        // When
        Map<String, Object> result = commentService.registComment(testCommentReqDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsKey("comments");
        assertThat(result.get("comments")).isEqualTo(updatedCommentList);
        assertThat(result).containsKey("totalComments");
        assertThat(result.get("totalComments")).isEqualTo(2L);

        verify(boardRepository, times(1)).existsById(boardId);
        verify(commentRepository, times(1)).save(any(Comment.class));
        verify(commentRepository, times(1)).findCommentByBoardIdWithUser(boardId, userId, firstPage);
        verify(pageUtil, times(1)).getPageInfo(updatedCommentPage, 10);
    }

    @Test
    @DisplayName("댓글 등록 실패 - 게시글 없음")
    void registComment_BoardNotFound() {
        // Given
        Long boardId = testCommentReqDto.getBoardId();

        // Mocking behavior
        when(boardRepository.existsById(boardId)).thenReturn(false); // 게시글 없음

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.registComment(testCommentReqDto);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus._DATA_NOT_FOUND);

        verify(boardRepository, times(1)).existsById(boardId);
        verify(commentRepository, never()).save(any(Comment.class));
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_Success() {
        // Given
        Long commentId = 1L;
        Long boardId = 10L;
        Long userId = 1L;
        int pageSize = 10;
        Sort sort = Sort.by(Sort.Direction.DESC, "id");
        Pageable firstPage = PageRequest.of(0, pageSize, sort);

        List<CommentRespDto> updatedCommentList = Collections.emptyList(); // 삭제 후 댓글이 없다고 가정
        Page<CommentRespDto> updatedCommentPage = new PageImpl<>(updatedCommentList, firstPage, 0);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("totalPage", 0);
        pageInfoMap.put("currentPage", 1);
        pageInfoMap.put("beginPage", 1);
        pageInfoMap.put("endPage", 1);

        // Mocking

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment));
        doNothing().when(commentRepository).deleteById(any(Long.class));
        when(commentRepository.findCommentByBoardIdWithUser(boardId, userId, firstPage))
                .thenReturn(updatedCommentPage);
        when(pageUtil.getPageInfo(any(Page.class), anyInt())).thenReturn(pageInfoMap);

        // When
        Map<String, Object> result = commentService.deleteComment(commentId, boardId, userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsKey("comments");
        assertThat(result.get("comments")).isEqualTo(Collections.emptyList());
        assertThat(result).containsKey("totalComments");
        assertThat(result.get("totalComments")).isEqualTo(0L);

        verify(commentRepository, times(1)).findById(testComment.getId());
        verify(commentRepository, times(1)).deleteById(testComment.getId());
        verify(commentRepository, times(1)).findCommentByBoardIdWithUser(boardId, userId, firstPage);
        verify(pageUtil, times(1)).getPageInfo(updatedCommentPage, 10);
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 댓글 없음")
    void deleteComment_NotFound() {
        // Given
        Long commentId = 99L;
        Long boardId = 10L;
        Long userId = 1L;

        // Mocking
        when(commentRepository.findById(commentId)).thenReturn(Optional.empty()); // 댓글 없음

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.deleteComment(commentId, boardId, userId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus._DATA_NOT_FOUND);

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).delete(any(Comment.class)); // 삭제 호출되지 않음
        verify(commentRepository, never()).findCommentByBoardIdWithUser(anyLong(), anyLong(), any(Pageable.class)); // 댓글 목록 조회도 호출되지 않음
    }

    @Test
    @DisplayName("댓글 삭제 실패 - 유저 정보 불일치")
    void deleteComment_Forbidden() {
        // Given
        Long commentId = 1L;
        Long boardId = 10L;
        Long userId = 99L; // 댓글 작성자와 다른 유저

        // Mocking behavior
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(testComment)); // 댓글은 존재

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            commentService.deleteComment(commentId, boardId, userId);
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus._FORBIDDEN); // Assuming _FORBIDDEN is added

        verify(commentRepository, times(1)).findById(commentId);
        verify(commentRepository, never()).delete(any(Comment.class)); // 삭제 호출되지 않음
        verify(commentRepository, never()).findCommentByBoardIdWithUser(anyLong(), anyLong(), any(Pageable.class)); // 댓글 목록 조회도 호출되지 않음
    }
}