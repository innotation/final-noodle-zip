package noodlezip.report.service;

import jakarta.persistence.EntityManager;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.community.entity.Comment;
import noodlezip.community.repository.CommentRepository;
import noodlezip.report.dto.ReportRequestDto;
import noodlezip.report.dto.ReportResponseDto;
import noodlezip.report.dto.ReportedCommentDto;
import noodlezip.report.entity.Report;
import noodlezip.report.repository.ReportRepository;
import noodlezip.report.status.ReportStatus;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private PageUtil pageUtil;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EntityManager em;

    @Mock
    private CommentRepository commentRepository;

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
    }

    @Test
    void findReportList_전체보기_정상작동() {
        // given
        List<ReportResponseDto> mockContent = List.of(
                new ReportResponseDto(1L, "user1", "게시글", 1L, "부적절한 내용입니다", "2025-07-03 14:33")
        );
        Page<ReportResponseDto> mockPage = new PageImpl<>(mockContent, pageable, 1);

        given(reportRepository.findReportList(pageable, "ALL")).willReturn(mockPage);
        Map<String, Object> pageInfoMap = new HashMap<>();
        pageInfoMap.put("totalCount", 1);
        pageInfoMap.put("page", 1);
        pageInfoMap.put("size", 5);
        pageInfoMap.put("pagePerBlock", 5);
        pageInfoMap.put("totalPage", 1);
        pageInfoMap.put("beginPage", 1);
        pageInfoMap.put("endPage", 1);
        pageInfoMap.put("isFirst", true);
        pageInfoMap.put("isLast", true);

        given(pageUtil.getPageInfo(mockPage, 5)).willReturn(pageInfoMap);

        // when
        Map<String, Object> result = reportService.findReportList(pageable, "ALL");

        // then
        assertThat(result.get("reportList")).isEqualTo(mockContent);
        assertThat(result.get("totalCount")).isEqualTo(1);
        assertThat(result.get("isFirst")).isEqualTo(true);
    }

    @Test
    void save_정상_동작() {
        // given
        Long userId = 1L;
        ReportRequestDto dto = ReportRequestDto.builder()
                .userId(userId)
                .reportType("게시글")
                .reportTargetId(10L)
                .content("부적절한 게시글입니다")
                .build();

        User mockUser = User.builder().id(userId).loginId("testuser").build();

        given(em.getReference(User.class, userId)).willReturn(mockUser);

        // when
        reportService.save(dto);

        // then
        ArgumentCaptor<Report> reportCaptor = ArgumentCaptor.forClass(Report.class);
        verify(reportRepository).save(reportCaptor.capture());

        Report saved = reportCaptor.getValue();
        assertThat(saved.getUser()).isEqualTo(mockUser);
        assertThat(saved.getReportType().getValue()).isEqualTo("게시글");
        assertThat(saved.getReportTargetId()).isEqualTo(10L);
        assertThat(saved.getContent()).isEqualTo("부적절한 게시글입니다");
        assertThat(saved.getReportStatus()).isEqualTo(ReportStatus.PENDING);
    }

    @Test
    void changeStatus_shouldUpdateReportStatus() {
        // given
        Long reportId = 1L;
        ReportStatus newStatus = ReportStatus.APPROVED;

        Report report = new Report();
        report.setReportStatus(ReportStatus.PENDING);

        when(reportRepository.findById(reportId)).thenReturn(Optional.of(report));

        // when
        reportService.changeStatus(reportId, newStatus);

        // then
        assertThat(report.getReportStatus()).isEqualTo(newStatus);
        verify(reportRepository).save(report);
    }

    @Test
    void changeStatus_shouldThrowException_whenReportNotFound() {
        // given
        Long invalidId = 999L;
        when(reportRepository.findById(invalidId)).thenReturn(Optional.empty());

        // expect
        assertThrows(IllegalArgumentException.class, () -> {
            reportService.changeStatus(invalidId, ReportStatus.REJECTED);
        });
    }

    // ✅ getReportedCommentById() 테스트
    @Test
    void getReportedCommentById_shouldReturnDto() {
        // given
        Long commentId = 1L;
        User user = User.builder().loginId("tester").build();
        Comment comment = Comment.builder()
                .id(commentId)
                .user(user)
                .content("신고된 댓글입니다.")
                .build();

        ReflectionTestUtils.setField(comment, "createdAt", LocalDateTime.of(2025, 7, 3, 12, 0));

        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // when
        ReportedCommentDto dto = reportService.getReportedCommentById(commentId);

        // then
        assertThat(dto.getLoginId()).isEqualTo("tester");
        assertThat(dto.getContent()).isEqualTo("신고된 댓글입니다.");
        assertThat(dto.getCreatedAt()).isEqualTo(comment.getCreatedAt());
    }

    @Test
    void getReportedCommentById_shouldThrowException_whenCommentNotFound() {
        // given
        Long invalidId = 123L;
        when(commentRepository.findById(invalidId)).thenReturn(Optional.empty());

        // expect
        assertThrows(CustomException.class, () -> {
            reportService.getReportedCommentById(invalidId);
        });
    }
}

