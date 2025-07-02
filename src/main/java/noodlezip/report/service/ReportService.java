package noodlezip.report.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.report.constant.ReportStatus;
import noodlezip.report.dto.ReportDto;
import noodlezip.report.entity.Report;
import noodlezip.report.repository.ReportRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public void save(ReportDto dto) {
        Report report = new Report();
        report.setUserId(dto.getUserId());
        report.setReportType(dto.getReportType());
        report.setReportTargetId(dto.getReportTargetId());
        report.setContent(dto.getContent());
        report.setReportStatus(ReportStatus.PENDING); // 상태는 기본값으로 고정

        log.info("신고 저장: {}", report);

        reportRepository.save(report);
    }


}
