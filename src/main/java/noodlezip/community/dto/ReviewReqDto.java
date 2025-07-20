package noodlezip.community.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.common.validation.ValidationGroups;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewReqDto {

    @NotNull
    private Long storeId;

    @NotNull(message = "제목은 비어있을 수 없습니다")
    @NotBlank(message = "제목은 비어있을 수 없습니다")
    private String title;

    @NotNull(message = "내용은 비어있을 수 없습니다")
    @NotBlank(message = "내용은 비어있을 수 없습니다")
    private String content;

    @NotBlank
    private String ocrKeyHash;

    @NotBlank
    private String visitDate;

    @NotNull
    @Size(min = 1, message = "리뷰는 최소 하나 이상 작성해야 합니다.")
    @Valid
    private List<MenuReviewDto> reviews;

    private Boolean isReceiptReview;
    private String imageUrl;

}
