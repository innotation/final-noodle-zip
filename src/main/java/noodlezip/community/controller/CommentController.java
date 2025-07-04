package noodlezip.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.dto.ApiResponse;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.validation.ValidationGroups;
import noodlezip.community.dto.CommentReqDto;
import noodlezip.community.dto.CommentRespDto;
import noodlezip.community.service.CommentService;
import noodlezip.community.status.BoardSuccessStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/comments")
@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComments(
            @AuthenticationPrincipal MyUserDetails user,
            @RequestParam("boardId") Long boardId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        Map<String, Object> map = commentService.findCommentList(boardId, user.getUser().getId(), pageable);
        return ApiResponse.onSuccess(BoardSuccessStatus._OK_GET_COMMENT, map);
    }

    @PostMapping("/regist")
    public ResponseEntity<ApiResponse<Map<String, Object>>> createComment(
            @AuthenticationPrincipal MyUserDetails user,
            @Validated(ValidationGroups.OnCreate.class) @ModelAttribute CommentReqDto commentReqDto,
            BindingResult bindingResult) {

        if (user != null && user.getUser() != null) {
            commentReqDto.setUserId(user.getUser().getId());
        } else {
            log.warn("비로그인 사용자가 댓글 등록 시도");
            return ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED);
        }

        if (bindingResult.hasErrors()) {
            log.warn("validate error: {}", bindingResult.getAllErrors());
            return ApiResponse.onFailure(ErrorStatus._BAD_REQUEST);
        }

        try {
            Map<String, Object> map = commentService.registComment(commentReqDto);
            return ApiResponse.onSuccess(BoardSuccessStatus._OK_COMMENT_ADDED, map);
        } catch (Exception e) {
            log.error("댓글 등록 중 오류 발생: {}", e.getMessage(), e);
            return ApiResponse.onFailure(ErrorStatus._INTERNAL_SERVER_ERROR);

        }
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<ApiResponse<?>> deleteComment(@PathVariable Long id, @AuthenticationPrincipal MyUserDetails user) {
//
//    }
}
