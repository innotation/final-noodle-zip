package noodlezip.report.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ReportType {
    POST("post"),
    COMMENT( "comment"),
    USER( "user");

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