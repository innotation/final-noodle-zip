package noodlezip.user.entity;

public enum UserType {

    NORMAL("일반"),
    ADMIN("관리자"),
    ;

    private final String name;

    UserType(String name) {
        this.name = name;
    }

    public static UserType fromString(String name) {
        for (UserType userType : UserType.values()) {
            if (userType.name.equals(name)) {
                return userType;
            }
        }
        throw new IllegalArgumentException("Unknown UserType value: " + name);
    }
}
