package noodlezip.report.service;

import jakarta.persistence.EntityManager;
import noodlezip.common.util.PageUtil;
import noodlezip.report.dto.ReportRequestDto;
import noodlezip.report.dto.ReportResponseDto;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

    private Pageable pageable;

    @BeforeEach
    void setUp() {
        pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
    }

    @Test
    void findReportList_전체보기_정상작동() {
        // given
        List<ReportResponseDto> mockContent = List.of(
                new ReportResponseDto("user1", "게시글", 1L, "부적절한 내용입니다", "2025-07-03 14:33")
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
}

