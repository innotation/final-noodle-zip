package noodlezip.mypage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MyCommunityType {

    POST_BOARD("boards"),
    LIKED_BOARD("liked-boards"),
    POST_COMMENT("comments");

    private final String path;

}
