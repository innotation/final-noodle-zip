package noodlezip.community.entity;

public enum CommunityActiveStatus {
        POSTED("게시"),
        HIDDEN("숨김"),
        REPORTED("신고"),
        ;

        private final String name;

    CommunityActiveStatus(String name) {
            this.name = name;
        }

        public static CommunityActiveStatus fromString(String name) {
            for (CommunityActiveStatus communityActiveStatus : CommunityActiveStatus.values()) {
                if (communityActiveStatus.name.equals(name)) {
                    return communityActiveStatus;
                }
            }
            throw new IllegalArgumentException("Unknown CommunityType value: " + name);
        }
}
