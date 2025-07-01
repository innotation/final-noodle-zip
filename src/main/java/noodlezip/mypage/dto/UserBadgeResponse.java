package noodlezip.mypage.dto;

import lombok.Getter;
import noodlezip.badge.constants.Region;

@Getter
public class UserBadgeResponse {

    private Integer badgeGroupId;
    private Integer badgeCategoryId;
    private Integer badgeId;

    private String badgeTitleName;  // optionName 역할
    private String badgeLevelName;  // 필요하면 유지
    private Integer accumulativeValue;

    private String badgeImageUrl;

    private Integer storeSidoLegalCode;
    private String ramenCategoryName;


    public UserBadgeResponse(Integer badgeId,
                             Integer badgeCategoryId,
                             Integer badgeGroupId,
                             String badgeTitleName,
                             String badgeLevelName,
                             Integer accumulativeValue,
                             String badgeImageUrl
    ) {
        this.badgeId = badgeId;
        this.badgeCategoryId = badgeCategoryId;
        this.badgeGroupId = badgeGroupId;
        this.badgeTitleName = badgeTitleName;
        this.badgeLevelName = badgeLevelName;
        this.accumulativeValue = accumulativeValue;
        this.badgeImageUrl = badgeImageUrl;
    }

    public UserBadgeResponse(Integer badgeId,
                             Integer badgeCategoryId,
                             Integer badgeGroupId,
                             String badgeTitleName,
                             String badgeLevelName,
                             Integer accumulativeValue,
                             String badgeImageUrl,
                             Integer storeSidoLegalCode,
                             String ramenCategoryName
    ) {
        this.badgeId = badgeId;
        this.badgeCategoryId = badgeCategoryId;
        this.badgeGroupId = badgeGroupId;
        this.badgeTitleName = badgeTitleName;
        this.badgeLevelName = badgeLevelName;
        this.accumulativeValue = accumulativeValue;
        this.badgeImageUrl = badgeImageUrl;
        this.storeSidoLegalCode = storeSidoLegalCode;
        this.ramenCategoryName = ramenCategoryName;

        if (ramenCategoryName != null) {
            this.badgeTitleName = ramenCategoryName;
        } else if (storeSidoLegalCode != null) {
            this.badgeTitleName = Region.getRegionBySidoCode(storeSidoLegalCode).getName();
        } else {
            this.badgeTitleName = badgeTitleName;
        }
    }

}