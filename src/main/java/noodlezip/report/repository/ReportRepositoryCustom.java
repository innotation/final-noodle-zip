package noodlezip.report.repository;

import noodlezip.report.dto.ReportResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ReportRepositoryCustom {

    Page<ReportResponseDto> findReportList(Pageable pageable, String type);
}
