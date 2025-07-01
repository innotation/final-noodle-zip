package noodlezip.badge.constants;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum LevelBadgeCategoryType {

    REVIEW_GET_LIKE_COUNT_BADGE(1L, false),
    COMMUNITY_GET_LIKE_COUNT_BADGE(2L, false),

    ALL_COMMUNITY_POST_COUNT_BADGE(3L, false),
    ALL_COMMENT_POST_COUNT_BADGE(4L, false),

    RAMEN_REVIEW_REGION_BADGE(5L, true),
    RAMEN_REVIEW_CATEGORY_BADGE(6L, true);

    private final Long dbPk;
    private final boolean isOptional;


    LevelBadgeCategoryType(Long dbPk, boolean isOptional) {
        this.dbPk = dbPk;
        this.isOptional = isOptional;
    }

    public static List<Long> getNotOptionBadgeCategoryIdList() {
        List<Long> result = new ArrayList<>();
        for (LevelBadgeCategoryType badgeCategoryType : LevelBadgeCategoryType.values()) {
            if(!badgeCategoryType.isOptional) {
                result.add(badgeCategoryType.getDbPk());
            }
        }
        return result;
    }

    public static List<Long> getOptionBadgeCategoryIdList() {
        List<Long> result = new ArrayList<>();
        for (LevelBadgeCategoryType badgeCategoryType : LevelBadgeCategoryType.values()) {
            if(badgeCategoryType.isOptional) {
                result.add(badgeCategoryType.getDbPk());
            }
        }
        return result;
    }

}
