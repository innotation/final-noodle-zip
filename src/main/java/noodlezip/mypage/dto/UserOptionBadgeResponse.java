package noodlezip.mypage.dto;

import noodlezip.badge.constants.Region;

public class UserOptionBadgeResponse {

    private Integer badgeGroupId;
    private Integer badgeCategoryId;
    private Integer badgeId;

    private String optionName;
    private String badgeName;
    private Integer accumulativeValue;

    private String badgeImageUrl;

    private Integer sidoLegalCode;
    private String ramenCategoryName;


    public UserOptionBadgeResponse(Integer accumulativeValue,
                                   Integer badgeCategoryId,
                                   Integer badgeGroupId,
                                   Integer badgeId,
                                   String badgeImageUrl,
                                   String badgeName,
                                   String ramenCategoryName,
                                   Integer sidoLegalCode
    ) {
        this.accumulativeValue = accumulativeValue;
        this.badgeCategoryId = badgeCategoryId;
        this.badgeGroupId = badgeGroupId;
        this.badgeId = badgeId;
        this.badgeImageUrl = badgeImageUrl;
        this.badgeName = badgeName;

        if (ramenCategoryName == null) {
            this.optionName = Region.getRegionBySidoCode(sidoLegalCode).getName();
        } else if (sidoLegalCode == null) {
            this.optionName = ramenCategoryName;
        }
    }

}
