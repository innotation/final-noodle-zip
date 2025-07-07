package noodlezip.report.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    POST("게시글"),
    COMMENT( "댓글"),
    USER( "사용자");

    private String value;

    public static ReportType fromValue(String value){
        for (ReportType reportType : ReportType.values()){
            if (reportType.value.equals(value)){
                return reportType;
            }
        }
        return null;
    }

}