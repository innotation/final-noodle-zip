package noodlezip.ocr.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum OcrErrorStatus implements BaseErrorCode {

    // ──────────────── OCR 관련 에러 ────────────────

    _FAIL_OCR_PROCESSING(HttpStatus.INTERNAL_SERVER_ERROR, "OCR-500", "영수증 인식에 실패했습니다."),
    _HASH_KEY_GENERATION_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "OCR-500-1", "OCR 해시 키 생성에 실패했습니다."),
    _EMPTY_RECEIPT_FILE(HttpStatus.BAD_REQUEST, "OCR-400-1", "영수증 파일이 비어 있습니다."),
    _INVALID_FILE_FORMAT(HttpStatus.BAD_REQUEST, "OCR-400-2", "지원하지 않는 파일 형식입니다."),
    _IMAGE_READ_FAIL(HttpStatus.UNPROCESSABLE_ENTITY, "OCR-422", "이미지 파일을 읽는 데 실패했습니다."),
    _NETWORK_ERROR(HttpStatus.BAD_GATEWAY, "OCR-502", "OCR 서버와의 통신에 실패했습니다."),
    _DUPLICATE_RECEIPT(HttpStatus.CONFLICT, "OCR-409", "이미 등록된 영수증입니다."),
    _STORE_NOT_FOUND(HttpStatus.NOT_FOUND, "OCR-404", "OCR 결과에 해당하는 매장을 찾을 수 없습니다."),
    _PARSE_FAIL(HttpStatus.UNPROCESSABLE_ENTITY, "OCR-422-2", "OCR 결과를 파싱하는 데 실패했습니다."),
    _OCR_GENERAL_FAIL(HttpStatus.BAD_REQUEST, "O0004", "영수증 인식에 실패했습니다.");

    // ──────────────── 필드 ────────────────

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