package noodlezip.mypage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MyCommunityType {

    POST_BOARD("POST_BOARD"),
    LIKED_BOARD("LIKED_BOARD"),
    POST_COMMENT("POST_COMMENT");

    private final String value;

}
