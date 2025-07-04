package noodlezip.savedstore.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SavedStoreErrorStatus implements BaseErrorCode {

    _FAIL_SAVED_STORE(HttpStatus.BAD_REQUEST,
            "MYPAGE-SAVED-STORE-001", "가게 저장에 실패하였습니다."),
    _FAIL_DELETED_STORE(HttpStatus.BAD_REQUEST,
            "MYPAGE-SAVED-STORE-002", "가게 삭제에 실패하였습니다."),
    _FAIL_UPDATE_SAVED_STORE_CATEGORY(HttpStatus.BAD_REQUEST,
            "MYPAGE-SAVED-STORE-003", "저장 가게 카테고리 수정에 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public final static String _DTO_FAIL_SAVED_STORE = "가게 저장에 실패했습니다.";
    public final static String _DTO_FAIL_UPDATE_SAVED_STORE_CATEGORY = "가게 저장 카테고리 업데이트에 실패했습니다.";
    public final static String _DTO_INVALID_SAVED_STORE_CATEGORY_NAME = "카테고리 이름은 최대 30자까지 입력할 수 있습니다.";
    public final static String _DTO_INVALID_SAVED_STORE_CATEGORY_MEMO = "메모는 최대 300자까지 입력할 수 있습니다.";

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
