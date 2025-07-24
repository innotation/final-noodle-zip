package noodlezip.mypage.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MyCommunityType {

    POST_BOARD("boards", "내가 작성한 게시글"),
    LIKED_BOARD("liked-boards", "내가 좋아요한 게시글"),
    POST_COMMENT("comments","내가 작성한 댓글");

    private final String path;
    private final String displayName;

}
