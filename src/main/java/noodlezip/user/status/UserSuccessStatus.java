package noodlezip.user.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseCode;
import noodlezip.common.dto.ReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum UserSuccessStatus implements BaseCode {

    _OK_EMAIL_NOT_DUPLICATE(HttpStatus.OK, "200", "이메일이 중복되지 않습니다."),
    _OK_LOGIN_ID_NOT_DUPLICATE(HttpStatus.OK,"200", "ID가 중복되지 않습니다.")
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDto getReason() {
        return ReasonDto.builder()
                .isSuccess(true)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return ReasonDto.builder()
                .isSuccess(true)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }
}
