package noodlezip.report.dto;

import lombok.*;
import noodlezip.report.status.ReportType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReportResponseDto {
    private Long id;
    private String loginId;
    private String reportType;
    private Long reportTargetId;
    private String content;
    private String createdAt;

    public ReportResponseDto(Long id,
                             String loginId,
                             ReportType reportTypeEnum,
                             Long reportTargetId,
                             String content,
                             String createdAt) {

        this.id             = id;
        this.loginId        = loginId;
        this.reportType     = reportTypeEnum.getValue();
        this.reportTargetId = reportTargetId;
        this.content        = content;
        this.createdAt      = createdAt;
    }
}
