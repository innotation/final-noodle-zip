package noodlezip.savedstore.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseCode;
import noodlezip.common.dto.ReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SavedStoreSuccessStatus implements BaseCode {

    _OK_SAVED_STORE(HttpStatus.OK,
            "MYPAGE-SAVED-STORE-001", "가게가 성공적으로 저장되었습니다."),
    _OK_DELETED_STORE(HttpStatus.OK,
            "MYPAGE-SAVED-STORE-002", "가게가 삭제되었습니다."),
    _OK_DELETED_SAVED_STORE_CATEGORY(HttpStatus.OK,
            "MYPAGE-SAVED-STORE-003", "저장 가게 카테고리가 성공적으로 삭제되었습니다."),
    _OK_UPDATE_SAVED_STORE_CATEGORY(HttpStatus.OK,
            "MYPAGE-SAVED-STORE-004", "저장 가게 카테고리가 업데이트 되었습니다.");

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
                .code(code)
                .message(message)
                .build();
    }

}
