package noodlezip.report.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class ReportRequestDto {
    private Long userId;
    private String reportType;
    private Long reportTargetId;
    private String content;
    private String reportStatus;
}