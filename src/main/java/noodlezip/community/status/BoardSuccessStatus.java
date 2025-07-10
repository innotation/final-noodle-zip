package noodlezip.community.status;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.common.code.BaseCode;
import noodlezip.common.dto.ReasonDto;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum BoardSuccessStatus implements BaseCode {

    _OK_GET_COMMENT(HttpStatus.OK, "COMMENT-001", "댓글 조회가 완료되었습니다."),
    _OK_COMMENT_ADDED(HttpStatus.OK, "COMMENT-002", "댓글이 추가되었습니다."),
    _OK_COMMENT_DELETED(HttpStatus.OK, "COMMENT-003", "댓글이 삭제되었습니다."),
    _OK_GET_BOARD(HttpStatus.OK, "BOARD-001", "게시글 조회가 완료되었습니다."),
    _OK_LIKED_CHANGED(HttpStatus.OK, "BOARD-002", "좋아요 상태가 변경되었습니다."),
    _OK_PHOTO_ADDED(HttpStatus.OK, "BOARD-003", "사진이 추가되었습니다.");


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
