package noodlezip.store.status;

import noodlezip.common.code.BaseCode;
import noodlezip.common.dto.ReasonDto;
import org.springframework.http.HttpStatus;

public enum StoreSuccessCode implements BaseCode {

    _SUCCESS_STORE_UPDATE(ReasonDto.builder()
            .httpStatus(HttpStatus.OK)
            .isSuccess(true)
            .code("ST000")
            .message("매장 정보가 성공적으로 수정되었습니다.")
            .build());

    private final ReasonDto reasonDto;

    StoreSuccessCode(ReasonDto reasonDto) {
        this.reasonDto = reasonDto;
    }

    @Override
    public ReasonDto getReason() {
        return reasonDto;
    }

    @Override
    public ReasonDto getReasonHttpStatus() {
        return reasonDto;
    }
}