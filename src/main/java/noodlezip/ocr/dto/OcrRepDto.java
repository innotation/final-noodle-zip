package noodlezip.ocr.dto;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrRepDto {
    private String ocrKeyHash;
    private OcrDataDto ocrData;
    private boolean isDuplicate;
}
