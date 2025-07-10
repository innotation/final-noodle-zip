package noodlezip.mypage.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum MyPageErrorStatus implements BaseErrorCode {

    _NOT_FOUND_USER_MY_PAGE(HttpStatus.NOT_FOUND,
            "MYPAGE-001", "페이지를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDto getReason() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return ErrorReasonDto.builder()
                .isSuccess(false)
                .httpStatus(httpStatus)
                .code(code)
                .message(message)
                .build();
    }

}
