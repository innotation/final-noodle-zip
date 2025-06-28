package noodlezip.badge.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BadgeErrorStatus implements BaseErrorCode {

    _NOT_FOUND_BADGE_CATEGORY(HttpStatus.INTERNAL_SERVER_ERROR,
            "BADGE-001", "배지 업데이트에 실패하였습니다. 존재하지 않는 배지 카테고리입니다."
    ),
    _NOT_FOUND_BADGE(HttpStatus.INTERNAL_SERVER_ERROR,
            "BADGE-002", "배지 생성에 실패하였습니다. 존재하지 않는 배지입니다."
    )
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDto getReason() {
        return null;
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return null;
    }

}
