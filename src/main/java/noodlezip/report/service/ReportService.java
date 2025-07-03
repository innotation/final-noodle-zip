package noodlezip.report.service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.util.PageUtil;
import noodlezip.report.dto.ReportRequestDto;
import noodlezip.report.dto.ReportResponseDto;
import noodlezip.report.entity.Report;
import noodlezip.report.repository.ReportRepository;
import noodlezip.report.status.ReportStatus;
import noodlezip.report.status.ReportType;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final PageUtil pageUtil;
    private final EntityManager em;

    @Transactional
    public void save(ReportRequestDto dto) {
        User user = em.getReference(User.class, dto.getUserId());

        Report report = new Report();
        report.setUser(user);
        report.setReportType(ReportType.fromValue(dto.getReportType()));
        report.setReportTargetId(dto.getReportTargetId());
        report.setContent(dto.getContent());
        report.setReportStatus(ReportStatus.PENDING); // 상태는 기본값으로 고정

        log.info("신고 저장: {}", report);

        reportRepository.save(report);
    }


    public Map<String, Object> findReportList(Pageable pageable, String type) {
        Page<ReportResponseDto> resultPage = reportRepository.findReportList(pageable, type);
        Map<String, Object> map = pageUtil.getPageInfo(resultPage, 5);
        map.put("reportList", resultPage.getContent());
        return map;
    }
}
