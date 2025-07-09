package noodlezip.badge.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BadgeErrorStatus implements BaseErrorCode {

    _NOT_FOUND_BADGE_CATEGORY(HttpStatus.INTERNAL_SERVER_ERROR,
            "BADGE-001", "존재하지 않는 배지 카테고리입니다."
    ),
    _NOT_FOUND_BADGE(HttpStatus.INTERNAL_SERVER_ERROR,
            "BADGE-002", "존재하지 않는 배지입니다."
    ),
    _NOT_FOUNT_USER_BADGE(HttpStatus.NOT_FOUND,
            "BADGE-003", "달성되지 않은 배지입니다."
    );

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
