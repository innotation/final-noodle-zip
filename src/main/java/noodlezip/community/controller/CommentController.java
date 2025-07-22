package noodlezip.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.dto.ErrorReasonDto;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.validation.ValidationGroups;
import noodlezip.community.dto.CommentReqDto;
import noodlezip.community.service.CommentService;
import noodlezip.community.status.BoardSuccessStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/comments")
@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "댓글 관리", description = "게시글 댓글 관련 API")
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    @Operation(summary = "댓글 목록 조회", description = "특정 게시글의 댓글 목록을 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 목록 조회 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: boardId 누락)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class)))
    })
    @Parameters({
            @Parameter(name = "boardId", description = "댓글을 조회할 게시글 ID", required = true, example = "1"),
            @Parameter(name = "page", description = "조회할 페이지 번호 (0부터 시작)", example = "0"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 댓글 개수", example = "10"),
            @Parameter(name = "sort", description = "정렬 기준 (예: id, createdAt)", example = "id,desc", hidden = true)
    })
    public ResponseEntity<noodlezip.common.dto.ApiResponse<Map<String, Object>>> getComments(
            @AuthenticationPrincipal MyUserDetails user,
            @RequestParam("boardId") Long boardId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {

        if (boardId == null) {
            log.warn("Board ID is null for comment retrieval.");
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        // pageable의 페이지 번호를 0부터 시작하도록 조정 (클라이언트가 1부터 보낼 경우)
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);

        try {
            // user가 null일 수 있으므로 null 체크 후 userId 전달
            Long userId = (user != null && user.getUser() != null) ? user.getUser().getId() : null;
            Map<String, Object> map = commentService.findCommentList(boardId, userId, pageable);
            return noodlezip.common.dto.ApiResponse.onSuccess(BoardSuccessStatus._OK_GET_COMMENT, map);
        } catch (CustomException e) {
            log.error("댓글 목록 조회 중 비즈니스 로직 오류 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("댓글 목록 조회 중 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/regist")
    @Operation(summary = "댓글 등록", description = "새로운 댓글을 게시글에 등록합니다. 로그인한 사용자만 가능합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 등록 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 또는 잘못된 요청",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (로그인 필요)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class)))
    })
    @Parameters({
            @Parameter(name = "commentReqDto", description = "댓글 내용을 포함하는 DTO", required = true,
                    schema = @Schema(implementation = CommentReqDto.class))
    })
    public ResponseEntity<noodlezip.common.dto.ApiResponse<Map<String, Object>>> createComment(
            @AuthenticationPrincipal MyUserDetails user,
            @Validated(ValidationGroups.OnCreate.class) @ModelAttribute CommentReqDto commentReqDto,
            BindingResult bindingResult) {

        commentReqDto.setUserId(user.getUser().getId());

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            log.warn("댓글 등록 유효성 검사 실패: {}", errorMessage);
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        try {
            Map<String, Object> map = commentService.registComment(commentReqDto);
            return noodlezip.common.dto.ApiResponse.onSuccess(BoardSuccessStatus._OK_COMMENT_ADDED, map);
        } catch (CustomException e) {
            log.error("댓글 등록 중 비즈니스 로직 오류 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("댓글 등록 중 서버 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @Operation(summary = "댓글 삭제", description = "지정된 ID의 댓글을 삭제합니다. 작성자 본인만 삭제할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ApiResponse.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (예: boardId 누락)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (로그인 필요)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "403", description = "권한 없음 (댓글 작성자가 아님)",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "404", description = "댓글 또는 게시글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 오류",
                    content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ErrorReasonDto.class)))
    })
    @Parameters({
            @Parameter(name = "id", description = "삭제할 댓글 ID", required = true, example = "10",
                    schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "boardId", description = "댓글이 속한 게시글 ID", required = true, example = "1"),
            @Parameter(hidden = true, name = "user", description = "현재 로그인된 사용자 정보 (Spring Security)")
    })
    public ResponseEntity<noodlezip.common.dto.ApiResponse<Map<String, Object>>> deleteComment(
            @PathVariable("id") Long commentId,
            @RequestParam("boardId") Long boardId,
            @AuthenticationPrincipal MyUserDetails user) {

        if (commentId == null || boardId == null) {
            log.warn("댓글 ID 또는 게시글 ID 누락 (commentId: {}, boardId: {})", commentId, boardId);
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        try {
            Map<String, Object> map = commentService.deleteComment(commentId, boardId, user.getUser().getId());
            return noodlezip.common.dto.ApiResponse.onSuccess(BoardSuccessStatus._OK_COMMENT_DELETED, map);
        } catch (CustomException e) {
            log.error("댓글 삭제 중 비즈니스 로직 오류 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("댓글 삭제 중 서버 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }
}