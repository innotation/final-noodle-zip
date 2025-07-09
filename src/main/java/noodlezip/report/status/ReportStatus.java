package noodlezip.report.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절");

    private String value;

    public static ReportStatus fromValue(String value){
        for(ReportStatus reportStatus : ReportStatus.values()){
            if(reportStatus.value.equals(value)){
                return reportStatus;
            }
        }
        return null;
    }
}
