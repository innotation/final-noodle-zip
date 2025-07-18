package noodlezip.ocr.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseCode;
import noodlezip.common.dto.ReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OcrSuccessStatus implements BaseCode {

    _OK_OCR_ANALYSIS(HttpStatus.OK, "OCR-001", "영수증 인식에 성공했습니다.");


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
