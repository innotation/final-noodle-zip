package noodlezip.community.status;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum CommunityType {
    COMMUNITY("community", "커뮤니티"),
    NOTICE("notice", "공지사항"),
    FREE("free", "자유게시판"),
    REVIEW("review", "리뷰"),
    QNA("qna", "질문과답변"),
    EVENT("event", "이벤트");

    private final String value;
    private final String displayName;

    public static CommunityType fromValue(String value) {
        for (CommunityType type : CommunityType.values()) {
            if (type.value.equals(value)) {
                return type;
            }
        }
        return null;
    }

    public static String getDisplayName(String value) {
        CommunityType type = fromValue(value);
        return type != null ? type.displayName : value;
    }
} 