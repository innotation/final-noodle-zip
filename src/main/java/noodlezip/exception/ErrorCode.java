package noodlezip.exception;

public enum ErrorCode {
    INVALID_INPUT("잘못된 입력입니다."),
    NOT_FOUND("데이터를 찾을 수 없습니다."),
    DUPLICATE("중복된 데이터입니다.");

    private final String message;
    ErrorCode(String message) { this.message = message; }
    public String getMessage() { return message; }
}
