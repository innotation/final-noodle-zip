package noodlezip.user.status;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import noodlezip.common.code.BaseErrorCode;
import noodlezip.common.dto.ErrorReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum UserErrorStatus implements BaseErrorCode {

    _NOT_FOUND_USER(HttpStatus.NOT_FOUND, "USER-001", "해당 사용자를 찾을 수 없습니다."),
    _EXPIRED_AUTH_CODE(HttpStatus.UNAUTHORIZED, "USER-002", "인증코드가 만료되었습니다. 인증코드를 재발급해주세요."),
    _MISS_MATCH_AUTH_CODE(HttpStatus.BAD_REQUEST, "USER-003", "인증코드가 일치하지 않습니다."),
    _EXISTING_USER_ACCOUNT_FACEBOOK(HttpStatus.FORBIDDEN, "USER-004", "이미 회원가입된 페이스북 계정이 존재합니다."),
    _EXISTING_USER_ACCOUNT_GOOGLE(HttpStatus.FORBIDDEN, "USER-005", "이미 회원가입된 구글 계정이 존재합니다."),
    _FORBIDDEN_ACCESS(HttpStatus.FORBIDDEN, "USER-006", "해당 요청을 수행할 권한이 없습니다."),
    _UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "USER-007", "인증되지 않은 접근입니다. 로그인 후 다시 시도해주세요."),
    _DUPLICATED_NICKNAME(HttpStatus.FORBIDDEN, "USER-008", "중복된 닉네임이 존재합니다. 다른 닉네임을 입력해주세요."),
    _ALREADY_EXIST_LOGIN_ID(HttpStatus.CONFLICT, "USER-009", "이미 사용 중인 아이디입니다."),
    _ALREADY_EXIST_EMAIL(HttpStatus.CONFLICT, "USER-010", "이미 사용 중인 이메일입니다."),
    _INVALID_PASSWORD_FORMAT(HttpStatus.BAD_REQUEST, "USER-011", "비밀번호는 최소 8자, 영문, 숫자, 특수문자를 포함해야 합니다."),
    _INVALID_PHONE_NUMBER_FORMAT(HttpStatus.BAD_REQUEST, "USER-012", "휴대폰 번호 형식이 올바르지 않습니다. (예: 010-1234-5678)"),
    _INVALID_BIRTH_DATE_FORMAT(HttpStatus.BAD_REQUEST, "USER-013", "생년월일 형식이 올바르지 않습니다. (예: YYYYMMDD)"),
    _INVALID_GENDER_VALUE(HttpStatus.BAD_REQUEST, "USER-014", "유효하지 않은 성별 값입니다."),
    _REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "USER-015", "회원가입 처리 중 알 수 없는 오류가 발생했습니다. 다시 시도해주세요."),
    _USER_INACTIVE(HttpStatus.FORBIDDEN, "USER-016", "사용자 계정이 비활성화 상태입니다.");

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

