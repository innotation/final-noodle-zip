package noodlezip.community.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReviewInitDto {
    private String bizNum;
    private String ocrKeyHash;
    private String visitDate;
    private Boolean isReceiptReview;
}
