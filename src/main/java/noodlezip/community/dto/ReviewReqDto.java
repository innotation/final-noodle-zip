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

    private Long storeId;
    private String title;
    private String content;
    private String ocrKeyHash;
    private String visitDate;
    private Boolean isReceiptReview;
    private String imageUrl;
    private List<MenuReviewDto> reviews;

}
