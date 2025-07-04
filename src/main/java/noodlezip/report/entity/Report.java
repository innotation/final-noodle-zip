package noodlezip.report.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;
import noodlezip.report.status.ReportStatus;
import noodlezip.report.status.ReportType;


@Entity
@Table(
        name = "tbl_report",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_target_type",
                columnNames = {"user_id", "report_target_id", "report_type"}
        )
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // 신고한 사용자 ID

    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false, length = 30)
    private ReportType reportType; // 게시글 / 댓글 / 유저

    @Column(name = "report_target_id", nullable = false)
    private Long reportTargetId; // 신고 대상 객체의 ID

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    private ReportStatus reportStatus;

}
