package noodlezip.community.service;

import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.FileUtil;
import noodlezip.common.util.PageUtil;
import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.entity.Board;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.repository.BoardRepository;
import noodlezip.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Transactional
class BoardServiceImplTest {
    @InjectMocks
    private BoardServiceImpl boardService;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private PageUtil pageUtil;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private FileUtil fileUtil;

    private User testUser;
    private Board testBoard;
    private BoardRespDto testBoardRespDto;
    private BoardReqDto testBoardReqDto;

    @BeforeEach
    void setUp() {
        // 테스트에 사용될 공통 객체 초기화
        testUser = User.builder()
                .id(1L)
                .loginId("test@example.com")
                .userName("testuser")
                .build();

        testBoard = Board.builder()
                .id(1L)
                .title("테스트 게시글")
                .content("테스트 내용")
                .communityType("community")
                .postStatus(CommunityActiveStatus.POSTED)
                .user(testUser)
                .imageUrl(null)
                .build();

        testBoardRespDto = BoardRespDto.builder()
                .boardId(1L)
                .userId(testUser.getId())
                .userName(testUser.getUserName())
                .title("테스트 게시글")
                .content("테스트 내용")
                .communityType("community")
                .postStatus(CommunityActiveStatus.POSTED)
                .build();

        testBoardReqDto = BoardReqDto.builder()
                .title("새 게시글")
                .content("새 게시글 내용")
                .build();
    }

    @Test
    @DisplayName("게시글 목록 조회 성공")
    void findBoardList_Success() {
        // Given
        Pageable pageable = PageRequest.of(0, 10);
        List<BoardRespDto> boardRespList = Arrays.asList(
                testBoardRespDto,
                BoardRespDto.builder().boardId(2L).title("게시글2").build()
        );
        Page<BoardRespDto> boardPage = new PageImpl<>(boardRespList, pageable, 2);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("totalPage", 1);
        pageInfoMap.put("currentPage", 1);
        pageInfoMap.put("beginPage", 1);
        pageInfoMap.put("endPage", 1);

        // Mocking
        when(boardRepository.findBoardWithPagination(any(Pageable.class))).thenReturn(boardPage);
        when(pageUtil.getPageInfo(any(Page.class), anyInt())).thenReturn(pageInfoMap);

        // When
        Map<String, Object> result = boardService.findBoardList(pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsKey("list");
        assertThat(result.get("list")).isEqualTo(boardRespList);
        assertThat(result).containsKey("totalPage");
        assertThat(result.get("totalPage")).isEqualTo(1);

        // Verify that repository and pageUtil methods were called
        verify(boardRepository, times(1)).findBoardWithPagination(pageable);
        verify(pageUtil, times(1)).getPageInfo(boardPage, 5);
    }

    @Test
    @DisplayName("카테고리별 게시글 목록 조회 성공")
    void findBoardListByCategory_Success() {
        // Given
        String category = "community";
        Pageable pageable = PageRequest.of(0, 10);
        List<BoardRespDto> boardRespList = Arrays.asList(
                testBoardRespDto,
                BoardRespDto.builder().boardId(2L).title("카테고리 게시글2").build()
        );
        Page<BoardRespDto> boardPage = new PageImpl<>(boardRespList, pageable, 2);

        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("totalPage", 1);
        pageInfoMap.put("currentPage", 1);
        pageInfoMap.put("beginPage", 1);
        pageInfoMap.put("endPage", 1);

        // Mocking
        when(boardRepository.findBoardWithPaginationAndCommunityType(anyString(), any(Pageable.class))).thenReturn(boardPage);
        when(pageUtil.getPageInfo(any(Page.class), anyInt())).thenReturn(pageInfoMap);

        // When
        Map<String, Object> result = boardService.findBoardListByCategory(category, pageable);

        // Then
        assertThat(result).isNotNull();
        assertThat(result).containsKey("list");
        assertThat(result.get("list")).isEqualTo(boardRespList);

        // Verify
        verify(boardRepository, times(1)).findBoardWithPaginationAndCommunityType(category, pageable);
        verify(pageUtil, times(1)).getPageInfo(boardPage, 5);
    }

    @Test
    @DisplayName("게시글 ID로 조회 성공")
    void findBoardById_Success() {
        // Given
        Long boardId = 1L;
        Long userId = 1L;

        // Mocking behavior
        when(boardRepository.findBoardByBoardIdWithUser(anyLong())).thenReturn(testBoardRespDto);

        // When
        BoardRespDto result = boardService.findBoardById(boardId, userId.toString());

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getBoardId()).isEqualTo(boardId);
        assertThat(result.getTitle()).isEqualTo("테스트 게시글");

        // Verify
        verify(boardRepository, times(1)).findBoardByBoardIdWithUser(boardId);
    }

    @Test
    @DisplayName("게시글 ID로 조회 실패 - 게시글 없음")
    void findBoardById_NotFound() {
        // Given
        Long boardId = 99L;
        Long userId = 1L;

        // Mocking
        when(boardRepository.findBoardByBoardIdWithUser(anyLong())).thenReturn(null);

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            boardService.findBoardById(boardId, userId.toString());
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus._DATA_NOT_FOUND);

        // Verify
        verify(boardRepository, times(1)).findBoardByBoardIdWithUser(boardId);
    }

    @Test
    @DisplayName("게시글 등록 성공 - 이미지 없음")
    void registBoard_NoImage_Success() {
        // Given
        MultipartFile mockImage = mock(MultipartFile.class);
        when(mockImage.isEmpty()).thenReturn(true); // 이미지 파일이 비어있음

        // Mocking
        when(modelMapper.map(any(BoardReqDto.class), eq(Board.class))).thenReturn(testBoard);
        when(boardRepository.save(any(Board.class))).thenReturn(testBoard); // save 호출 시 testBoard 반환

        // When
        boardService.registBoard(testBoardReqDto, testUser, mockImage);

        // Then
        verify(boardRepository, times(1)).save(testBoard);
        // Verify
        verify(fileUtil, never()).fileupload(anyString(), any(MultipartFile.class));

        assertThat(testBoard.getCommunityType()).isEqualTo("community");
        assertThat(testBoard.getPostStatus()).isEqualTo(CommunityActiveStatus.POSTED);
        assertThat(testBoard.getUser()).isEqualTo(testUser);
        assertThat(testBoard.getImageUrl()).isNull(); // 이미지가 없으므로 imageUrl은 null
    }

    @Test
    @DisplayName("게시글 등록 성공 - 이미지 있음")
    void registBoard_WithImage_Success() {
        // Given
        MultipartFile mockImage = mock(MultipartFile.class);
        String originalFilename = "test_image.jpg";
        String fileUrl = "http://example.com/test_image.jpg";
        Map<String, String> fileUploadResult = new HashMap<>();
        fileUploadResult.put("fileUrl", fileUrl);

        when(mockImage.isEmpty()).thenReturn(false);
        when(mockImage.getOriginalFilename()).thenReturn(originalFilename);

        // Mocking
        when(modelMapper.map(any(BoardReqDto.class), eq(Board.class))).thenReturn(testBoard);
        when(fileUtil.fileupload(anyString(), any(MultipartFile.class))).thenReturn(fileUploadResult);
        when(boardRepository.save(any(Board.class))).thenReturn(testBoard);

        // When
        boardService.registBoard(testBoardReqDto, testUser, mockImage);

        // Then
        verify(modelMapper, times(1)).map(testBoardReqDto, Board.class);
        verify(fileUtil, times(1)).fileupload("board", mockImage); // fileUtil 호출 확인
        verify(boardRepository, times(1)).save(testBoard);

        assertThat(testBoard.getImageUrl()).isEqualTo(fileUrl); // imageUrl이 설정 확인
    }

    @Test
    @DisplayName("게시글 삭제 성공")
    void deleteBoard_Success() {
        // Given
        Long boardId = 1L;

        // Mocking behavior
        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));
        doNothing().when(boardRepository).delete(any(Board.class)); // delete는 void 메서드이므로 doNothing 사용

        // When
        boardService.deleteBoard(boardId, testUser.getId());

        // Then
        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, times(1)).delete(testBoard);
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 게시글 ID 없음")
    void deleteBoard_NotFound() {
        // Given
        Long boardId = 99L;

        // Mocking behavior
        when(boardRepository.findById(anyLong())).thenReturn(Optional.empty()); // 게시글을 찾지 못함

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            boardService.deleteBoard(boardId, testUser.getId());
        });

        assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus._DATA_NOT_FOUND);

        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, never()).delete(any(Board.class));
    }

    @Test
    @DisplayName("게시글 삭제 실패 - 유저 정보 불일치")
    void deleteBoard_Forbidden() {
        // Given
        Long boardId = 1L;
        Long requestingUserId = 99L;

        when(boardRepository.findById(anyLong())).thenReturn(Optional.of(testBoard));

        // When & Then
        CustomException exception = assertThrows(CustomException.class, () -> {
            boardService.deleteBoard(boardId, requestingUserId); // userId 파라미터 전달
        });
        assertThat(exception.getErrorCode()).isEqualTo(ErrorStatus._FORBIDDEN);

        // verify
        verify(boardRepository, times(1)).findById(boardId);
        verify(boardRepository, never()).delete(any(Board.class));
    }
}