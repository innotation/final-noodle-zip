package noodlezip.community.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.validation.ValidationGroups;
import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.service.BoardService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "게시판 관리", description = "게시글 조회, 등록, 상세 보기 관련 API")
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/review")
    @Operation(summary = "리뷰 작성 페이지", description = "사용자가 리뷰를 작성할 수 있는 HTML 폼 페이지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 작성 페이지 반환 성공",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    public String review() {
        return "/board/leave-review";
    }

    @GetMapping("/registBoard")
    @Operation(summary = "게시글 등록 폼 페이지", description = "새로운 게시글을 작성하기 위한 HTML 폼 페이지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 등록 페이지 반환 성공",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    public void registBoard() {}

    @PostMapping("/regist")
    @Operation(summary = "게시글 등록 처리", description = "새로운 게시글을 등록합니다. 로그인한 사용자만 가능하며, 이미지 파일 첨부를 지원합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "게시글 등록 성공 및 게시글 목록 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "유효성 검사 실패 (제목 또는 내용 누락 등)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (로그인 필요)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류 (파일 업로드 실패 등)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    @Parameters({
            @Parameter(name = "user", description = "현재 로그인된 사용자 정보 (Spring Security에서 주입)", hidden = true),
            @Parameter(name = "boardReqDto", description = "게시글의 제목과 내용을 포함하는 요청 DTO", required = true,
                    schema = @Schema(implementation = BoardReqDto.class)),
            @Parameter(name = "bindingResult", description = "유효성 검사 결과", hidden = true),
            @Parameter(name = "boardImage", description = "게시글에 첨부할 이미지 파일 (선택 사항)", required = false,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    })
    public String regist(
            @AuthenticationPrincipal MyUserDetails user,
            @Validated(ValidationGroups.OnCreate.class) @ModelAttribute BoardReqDto boardReqDto,
            BindingResult bindingResult,
            @RequestParam(value = "boardImage", required = false) MultipartFile boardImage) {

        // 1. 사용자 인증 확인
        if (user == null || user.getUser() == null) {
            log.warn("비로그인 사용자가 게시글 등록 시도.");
            throw new CustomException(ErrorStatus._UNAUTHORIZED);
        }

        if (bindingResult.hasErrors()) {
            String errorMessage = bindingResult.getFieldErrors().stream()
                    .map(FieldError::getDefaultMessage)
                    .collect(Collectors.joining(", "));
            log.error("게시글 작성 유효성 검사 실패: {}", errorMessage);
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        try {
            boardService.registBoard(boardReqDto, user.getUser(), boardImage);
            return "redirect:/board/list";
        } catch (CustomException e) {
            log.error("게시글 등록 중 비즈니스 로직 오류 발생: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("게시글 등록 중 예상치 못한 서버 오류 발생: {}", e.getMessage(), e);
            throw new CustomException(ErrorStatus._INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping({"/list", "/{category}/list"})
    @Operation(summary = "게시글 목록 조회", description = "모든 게시글 또는 특정 카테고리의 게시글을 최신 순으로 페이지네이션하여 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 목록 조회 성공",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    @Parameters({
            @Parameter(name = "category", description = "조회할 게시글의 카테고리 (선택 사항). 예: 'community', 'qna'", required = false, example = "community"),
            @Parameter(name = "page", description = "조회할 페이지 번호 (기본값: 0, 1부터 요청 시 내부적으로 0으로 변환)", example = "1"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 게시글 개수 (기본값: 6)", example = "6"),
            @Parameter(name = "sort", description = "정렬 기준 (예: id, createdAt). 기본값은 id,desc", example = "id,desc", hidden = true),
            @Parameter(name = "model", description = "View로 데이터를 전달하기 위한 Spring Model 객체", hidden = true)
    })
    public String list(
            @PathVariable(value = "category", required = false) Optional<String> categoryOptional,
            @PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable,
            Model model) {

        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);

        String category = null;

        try {
            Map<String, Object> map;
            if (categoryOptional.isPresent() && !categoryOptional.get().isEmpty()) {
                category = categoryOptional.get();
                log.info("특정 카테고리 게시글 목록 조회 요청: {}", category);
                map = boardService.findBoardListByCategory(category, pageable);
            } else {
                // 전체 게시글 목록 조회
                log.info("전체 게시글 목록 조회 요청");
                map = boardService.findBoardList(pageable);
            }

            model.addAttribute("category", category);
            model.addAttribute("board", map.get("list"));
            model.addAttribute("page", map.get("page"));
            model.addAttribute("beginPage", map.get("beginPage"));
            model.addAttribute("endPage", map.get("endPage"));
            model.addAttribute("isFirst", map.get("isFirst"));
            model.addAttribute("isLast", map.get("isLast"));

            return "/board/list"; // HTML 템플릿 경로 반환
        } catch (CustomException e) {
            log.error("게시글 목록 조회 중 비즈니스 로직 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "/error/general-error"; // 오류 발생 시 보여줄 HTML 템플릿 (적절히 변경 필요)
        } catch (Exception e) {
            log.error("게시글 목록 조회 중 예상치 못한 서버 오류 발생: {}", e.getMessage(), e);
            model.addAttribute("errorMessage", "게시글 목록을 불러오는 중 예상치 못한 오류가 발생했습니다.");
            return "/error/general-error"; // 오류 발생 시 보여줄 HTML 템플릿
        }
    }

    @GetMapping("/detail/{id}")
    @Operation(summary = "게시글 상세 조회", description = "특정 게시글의 상세 내용을 조회하고 조회수를 증가시킵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 상세 조회 성공",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 (게시글 ID 누락 또는 유효하지 않음)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    @Parameters({
            @Parameter(name = "id", description = "조회할 게시글의 ID", required = true, example = "1",
                    schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "model", description = "View로 데이터를 전달하기 위한 Spring Model 객체", hidden = true)
    })
    public String board(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal MyUserDetails user,
            HttpServletRequest request,
            Model model) {

        if (id == null || id <= 0) {
            log.warn("유효하지 않은 게시글 ID로 상세 조회 시도: {}", id);
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        String userIdOrIp;

        if (user != null) {
            userIdOrIp = "user:" + user.getUser().getId();
        } else {
            userIdOrIp = "ip:" + getClientIp(request);
        }
        try {
            BoardRespDto board = boardService.findBoardById(id, userIdOrIp);
            if (board == null) {
                log.warn("존재하지 않는 게시글 ID로 상세 조회 시도: {}", id);
                throw new CustomException(ErrorStatus._DATA_NOT_FOUND);
            }
            model.addAttribute("board", board);
            return "/board/detail";
        } catch (CustomException e) {
            log.error("게시글 상세 조회 중 비즈니스 로직 오류 발생 (ID: {}): {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", e.getMessage());
            return "/error/general-error";
        } catch (Exception e) {
            log.error("게시글 상세 조회 중 예상치 못한 서버 오류 발생 (ID: {}): {}", id, e.getMessage(), e);
            model.addAttribute("errorMessage", "게시글을 불러오는 중 예상치 못한 오류가 발생했습니다.");
            return "/error/general-error";
        }
    }

    @PostMapping("/delete/{boardId}")
    @Operation(summary = "게시글 삭제", description = "지정된 ID의 게시글을 삭제합니다. 게시글 작성자만 삭제할 수 있습니다.",
            method = "POST")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "302", description = "게시글 삭제 성공 및 게시글 목록 페이지로 리다이렉트",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자 (로그인 필요)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "403", description = "권한 없음 (게시글 작성자가 아님)",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "404", description = "게시글을 찾을 수 없음",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE)),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    @Parameters({
            @Parameter(name = "boardId", description = "삭제할 게시글의 ID", required = true, example = "1",
                    schema = @Schema(type = "integer", format = "int64")),
            @Parameter(name = "user", description = "현재 로그인된 사용자 정보 (Spring Security에서 주입)", hidden = true)
    })
    public String deleteBoard(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal MyUserDetails user) {
        boardService.deleteBoard(boardId, user.getUser().getId());
        return "redirect:/board/list";
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}