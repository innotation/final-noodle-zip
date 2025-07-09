package noodlezip.community.service;

public enum TargetType {
    BOARD("board"),
    COMMENT("comment"),
    USER("user");

    private final String value;

    TargetType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
