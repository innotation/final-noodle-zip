package noodlezip.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.dto.ApiResponse;
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
@Controller
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ApiResponse<Map<String, Object>>> getComments(
            @RequestParam("boardId") Long boardId,
            @PageableDefault(size = 10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);
        Map<String, Object> map = commentService.findCommentList(boardId, pageable);
        return ApiResponse.onSuccess(BoardSuccessStatus._OK_GET_COMMENT, map);
    }

    @PostMapping("/regist")
    public String createComment(@AuthenticationPrincipal MyUserDetails user
                                , @Validated(ValidationGroups.OnCreate.class) @ModelAttribute CommentReqDto commentReqDto
                                , BindingResult bindingResult) {

        if (user != null && user.getUser() != null) {
            commentReqDto.setUserId(user.getUser().getId());
        } else {
            log.warn("비로그인 사용자가 댓글 등록 시도");
            return "redirect:/login";
        }

        if (bindingResult.hasErrors()) {
            log.warn("validate error: {}", bindingResult.getAllErrors());
            return "redirect:/";
        }

        try {
            commentService.registComment(commentReqDto);
        } catch (Exception e) {
            log.error("댓글 등록 중 오류 발생: {}", e.getMessage(), e);
        }

        return "redirect:/board/detail/" + commentReqDto.getBoardId();
    }
}
