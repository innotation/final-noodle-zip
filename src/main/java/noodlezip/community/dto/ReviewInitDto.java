package noodlezip.community.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ReviewInitDto {
    private Long bizNum;
    private String ocrKeyHash;
    private String visitDate;
}
