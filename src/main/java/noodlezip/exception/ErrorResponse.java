package noodlezip.exception;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {
    private int status;
    private String message;
    private String code;
    private LocalDateTime timestamp;

    public static ErrorResponse of(ErrorCode code) {
        return ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(code.getMessage())
                .code(code.name())
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static ErrorResponse of(int status, String message, String code) {
        return ErrorResponse.builder()
                .status(status)
                .message(message)
                .code(code)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
