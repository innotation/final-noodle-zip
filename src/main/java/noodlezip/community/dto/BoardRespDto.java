package noodlezip.community.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.*;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.store.dto.StoreReviewDto;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class BoardRespDto {

    private Long boardId;
    private Long userId;
    private String userName;
    private String userProfileImageUrl;
    private String title;
    private String content;
    private String communityType;
    private CommunityActiveStatus postStatus;
    private int likesCount;
    private int viewsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String imageUrl;
    private Boolean isWriter;
    private Boolean isLike;
    private List<StoreReviewDto> menuReviews;

}
