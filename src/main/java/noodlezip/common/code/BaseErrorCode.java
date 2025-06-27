package noodlezip.common.code;

import noodlezip.common.dto.ErrorReasonDto;

public interface BaseErrorCode {
    ErrorReasonDto getReason();
    ErrorReasonDto getReasonHttpStatus();
}
