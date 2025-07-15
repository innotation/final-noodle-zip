package noodlezip.community.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReqDto {

    private String title;
    private String content;
    private String ocrKeyHash;
    private String visitDate;
    private Long bizNum;
    private List<MenuReviewDto> reviews;

}
