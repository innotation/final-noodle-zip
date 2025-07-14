package noodlezip.store.status;

import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

public enum StoreErrorCode implements BaseErrorCode {

    _DUPLICATE_MENU_NAME(ErrorReasonDto.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .isSuccess(false)
            .code("ST001")
            .message("중복된 메뉴 이름입니다.")
            .build()),

    _STORE_NOT_FOUND(ErrorReasonDto.builder()
            .httpStatus(HttpStatus.NOT_FOUND)
            .isSuccess(false)
            .code("ST002")
            .message("해당 매장을 찾을 수 없습니다.")
            .build()),

    _CANNOT_USE_DEFAULT_TOPPING(ErrorReasonDto.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .isSuccess(false)
            .code("ST003")
            .message("기본 토핑은 추가 토핑으로 사용할 수 없습니다.")
            .build()),

    _UNKNOWN_TOPPING_NAME(ErrorReasonDto.builder()
            .httpStatus(HttpStatus.BAD_REQUEST)
            .isSuccess(false)
            .code("ST004")
            .message("존재하지 않는 토핑 이름입니다.")
            .build()),

    _FORBIDDEN(ErrorReasonDto.builder()
            .httpStatus(HttpStatus.FORBIDDEN)
            .isSuccess(false)
            .code("ST005")
            .message("권한이 없습니다.")
            .build());


    private final ErrorReasonDto errorReasonDto;

    StoreErrorCode(ErrorReasonDto errorReasonDto) {
        this.errorReasonDto = errorReasonDto;
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