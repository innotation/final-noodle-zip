package noodlezip.store.status;

import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

public enum StoreErrorCode implements BaseErrorCode {

    _DUPLICATE_MENU_NAME(HttpStatus.BAD_REQUEST, false, "ST001", "중복된 메뉴 이름입니다."),
    _STORE_NOT_FOUND(HttpStatus.NOT_FOUND, false, "ST002", "해당 매장을 찾을 수 없습니다."),
    _CANNOT_USE_DEFAULT_TOPPING(HttpStatus.BAD_REQUEST, false, "ST003", "기본 토핑은 추가 토핑으로 사용할 수 없습니다."),
    _UNKNOWN_TOPPING_NAME(HttpStatus.BAD_REQUEST, false, "ST004", "존재하지 않는 토핑 이름입니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, false, "ST005", "권한이 없습니다."),
    _MENU_NOT_FOUND(HttpStatus.NOT_FOUND, false, "ST006", "해당 메뉴를 찾을 수 없습니다.");

    private final ErrorReasonDto errorReasonDto;

    StoreErrorCode(HttpStatus status, boolean isSuccess, String code, String message) {
        this.errorReasonDto = ErrorReasonDto.builder()
                .httpStatus(status)
                .isSuccess(isSuccess)
                .code(code)
                .message(message)
                .build();
    }

    @Override
    public ErrorReasonDto getReason() {
        return errorReasonDto;
    }

    @Override
    public ErrorReasonDto getReasonHttpStatus() {
        return errorReasonDto;
    }
}