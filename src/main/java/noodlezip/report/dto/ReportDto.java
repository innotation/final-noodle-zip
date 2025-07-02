package noodlezip.report.dto;

import lombok.*;
import noodlezip.report.constant.ReportStatus;
import noodlezip.report.constant.ReportType;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ReportDto {
    private Long userId;
    private ReportType reportType;
    private Long reportTargetId;
    private String content;
    private ReportStatus reportStatus;
}