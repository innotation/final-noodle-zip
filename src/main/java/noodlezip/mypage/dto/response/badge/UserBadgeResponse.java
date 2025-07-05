package noodlezip.mypage.dto.response.badge;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserBadgeResponse {

    private Long userBadgeId;
    private Long badgeId;
    private Long badgeCategoryId;
    private Long badgeGroupId;

    private String badgeTitleName;
    private String badgeLevelName;
    private Integer accumulativeValue;
    private String badgeImageUrl;

}