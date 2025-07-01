package noodlezip.mypage.dto;

import lombok.*;
import noodlezip.mypage.dto.response.UserBadgeResponse;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserNoOptionBadgeDto {

    private Long userBadgeId;
    private Long badgeId;
    private Long badgeCategoryId;
    private Long badgeGroupId;
    private String badgeCategoryName;
    private String badgeName;
    private Integer accumulativeValue;
    private String badgeImageUrl;


    public UserBadgeResponse toUserBadgeResponse() {
        return UserBadgeResponse.builder()
                .userBadgeId(userBadgeId)
                .badgeId(badgeId)
                .badgeCategoryId(badgeCategoryId)
                .badgeGroupId(badgeGroupId)
                .badgeTitleName(badgeCategoryName)
                .badgeLevelName(badgeName)
                .accumulativeValue(accumulativeValue)
                .badgeImageUrl(badgeImageUrl)
                .build();
    }

}
