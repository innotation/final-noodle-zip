package noodlezip.user.entity;

public enum ActiveStatus {
    ACTIVE("정상"),
    SIGNOUT("탈퇴"),
    STOP("정지"),
    ;

    private final String name;

    ActiveStatus(String name) {
        this.name = name;
    }

    public static ActiveStatus fromString(String name) {
        for (ActiveStatus activeStatus : ActiveStatus.values()) {
            if (activeStatus.name.equals(name)) {
                return activeStatus;
            }
        }
        throw new IllegalArgumentException("Unknown UserType value: " + name);
    }
}
