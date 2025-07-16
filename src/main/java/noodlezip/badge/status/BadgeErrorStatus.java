package noodlezip.badge.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public enum BadgeErrorStatus implements BaseErrorCode {

    _NOT_FOUND_BADGE_CATEGORY(HttpStatus.BAD_REQUEST,
            "BADGE-001", "존재하지 않는 배지 카테고리입니다."),
    _NOT_FOUND_BADGE(HttpStatus.BAD_REQUEST,
            "BADGE-002", "존재하지 않는 배지입니다."),
    _NOT_FOUNT_USER_BADGE(HttpStatus.NOT_FOUND,
            "BADGE-003", "달성되지 않은 배지입니다."),
    _NOT_FOUNT_USER_BADGE_PAGE(HttpStatus.NOT_FOUND,
            "BADGE-004", "존재하지 않는 배지 페이지입니다."),
    _NOT_FOUNT_REGION(HttpStatus.NOT_FOUND,
            "BADGE-005", "배지 서비스가 지원하지 않는 지역입니다.")
    ;


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
