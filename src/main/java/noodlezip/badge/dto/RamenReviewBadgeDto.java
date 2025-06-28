package noodlezip.badge.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

///리뷰 게시글의 로젝에 따라 담는 값이 달라짐 - 최대한 배지 로직 내부에서 만들어서 사용할 수있도롣-> 최대한 심어둔 메서드에 로직이 없도록 해야한다.(배지와 기능 분리)
public class RamenReviewBadgeDto implements BadgeExtraOptionDto {

    private Long storeId;
    private List<Long> menuIdList;

}
