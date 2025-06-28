package noodlezip.badge.constants;

import lombok.Getter;

@Getter
public enum LevelBadgeCategoryType {

    REVIEW_GET_LIKE_COUNT_BADGE(1L),
    COMMUNITY_GET_LIKE_COUNT_BADGE(2L),

    ALL_COMMUNITY_POST_COUNT_BADGE(3L),
    ALL_COMMENT_POST_COUNT_BADGE(4L),

    RAMEN_REVIEW_REGION_BADGE(5L),
    RAMEN_REVIEW_CATEGORY_BADGE(6L);

    private final Long dbPk;


    LevelBadgeCategoryType(Long dbPk) {
        this.dbPk = dbPk;
    }

}
