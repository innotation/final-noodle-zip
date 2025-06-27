package noodlezip.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import noodlezip.common.code.BaseErrorCode;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class CustomException extends RuntimeException {
    private final BaseErrorCode errorCode;

    @Override
    public String getMessage() {
        return errorCode.getReasonHttpStatus().getMessage();
    }

    public String getCode() {
        return errorCode.getReasonHttpStatus().getCode();
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getReasonHttpStatus().getHttpStatus();
    }
}
