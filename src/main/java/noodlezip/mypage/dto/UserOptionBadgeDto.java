package noodlezip.mypage.dto;

import lombok.*;
import noodlezip.badge.constants.Region;
import noodlezip.mypage.dto.response.UserBadgeResponse;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserOptionBadgeDto {

    private Long userBadgeId;
    private Long badgeId;
    private Long badgeCategoryId;
    private Long badgeGroupId;
    private Integer storeSidoLegalCode;
    private String ramenCategoryName;
    private String badgeName;
    private Integer accumulativeValue;
    private String badgeImageUrl;


    public UserBadgeResponse toUserBadgeResponse() {
        String badgeTitleName = "";
        if (storeSidoLegalCode == null) {
            badgeTitleName = ramenCategoryName;
        } else if (ramenCategoryName == null) {
            badgeTitleName = Region.getRegionBySidoCode(storeSidoLegalCode).getName();
        }

        return UserBadgeResponse.builder()
                .userBadgeId(userBadgeId)
                .badgeId(badgeId)
                .badgeCategoryId(badgeCategoryId)
                .badgeGroupId(badgeGroupId)
                .badgeTitleName(badgeTitleName)
                .badgeLevelName(badgeName)
                .accumulativeValue(accumulativeValue)
                .badgeImageUrl(badgeImageUrl)
                .build();
    }

}
