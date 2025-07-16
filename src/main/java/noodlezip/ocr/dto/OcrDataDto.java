package noodlezip.ocr.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OcrDataDto {
    private String storeName;
    private String date;
    private List<String> menuNames;
    private String bizNum;
}