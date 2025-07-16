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
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.common.util.CookieUtil;
import noodlezip.common.util.RequestParserUtil;
import noodlezip.common.validation.ValidationGroups;
import noodlezip.community.dto.*;
import noodlezip.community.entity.Board;
import noodlezip.community.entity.BoardUserId;
import noodlezip.community.service.BoardService;
import noodlezip.community.status.BoardSuccessStatus;
import noodlezip.store.dto.OcrToReviewDto;
import noodlezip.store.entity.Store;
import noodlezip.store.service.StoreService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.*;
import java.util.stream.Collectors;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
@Tag(name = "게시판 관리", description = "게시글 조회, 등록, 상세 보기 관련 API")
public class BoardController {

    private final BoardService boardService;
    private final RequestParserUtil requestParserUtil;

    private static final String RECENT_VIEWED_BOARDS = "recentViewedBoards";
    private static final int MAX_RECENT_BOARDS = 3;
    private final StoreService storeService;

    @RequestMapping(value = "/review", method = {RequestMethod.GET, RequestMethod.POST})
    @Operation(summary = "리뷰 작성 페이지", description = "사용자가 리뷰를 작성할 수 있는 HTML 폼 페이지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "리뷰 작성 페이지 반환 성공",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    public String review(@ModelAttribute ReviewInitDto reviewInitDto,
                         Model model) {
//      ==================테스트용 임시 값 확인작업 맘무리 후 삭제===========================
        if (reviewInitDto.getBizNum() == null) {
            reviewInitDto.setBizNum("4578502690");
            reviewInitDto.setVisitDate("2025-07-07");
            reviewInitDto.setOcrKeyHash("6a45e2d28f11b953e9e5913b33ed9848f1d6bfa6112b1b5e5aa83ee7268a126d");
        }

        OcrToReviewDto ocrToReviewDto = storeService.findStoreWithMenusByBizNum(Long.valueOf(reviewInitDto.getBizNum()));
        model.addAttribute("storeId", ocrToReviewDto.getStoreId());
        model.addAttribute("storeName",ocrToReviewDto.getStoreName());
        model.addAttribute("menuList", ocrToReviewDto.getMenuList());
        model.addAttribute("toppings", ocrToReviewDto.getToppingList());

        model.addAttribute("reviewInitDto", reviewInitDto);
        return "/board/leave-review";
    }

    @PostMapping("/registReviewJson")
    public ResponseEntity<Map<String, Object>> registReview(@RequestBody ReviewReqDto dto,
                                                            @AuthenticationPrincipal MyUserDetails userDetails) {
        List<Long> reviewIds = boardService.saveReviewJson(dto, userDetails.getUser());
        Map<String, Object> response = new HashMap<>();
        response.put("reviewIds", reviewIds); // 각 슬라이드에 해당하는 리뷰 ID 목록 반환
        return ResponseEntity.ok(response);
    }

    @PostMapping("/uploadReviewImages")
    public ResponseEntity<Void> uploadReviewImages(@RequestParam Map<String, MultipartFile> files) {
        files.forEach((key, file) -> {
            if (key.startsWith("image_")) {
                Long reviewId = Long.parseLong(key.substring("image_".length()));
                boardService.saveReviewImage(reviewId, file); // 리뷰 ID 기준으로 이미지 저장
            }
        });
        return ResponseEntity.ok().build();
    }

    @GetMapping("/registBoard")
    @Operation(summary = "게시글 등록 폼 페이지", description = "새로운 게시글을 작성하기 위한 HTML 폼 페이지를 반환합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "게시글 등록 페이지 반환 성공",
                    content = @Content(mediaType = MediaType.TEXT_HTML_VALUE))
    })
    public void registBoard() {
    }

    @PostMapping("/regist")
    @Operation(summary = "게시글 등록 처리", description = "새로운 게시글을 등록합니다. 로그인한 사용자만 가능하며, 이미지 파일 첨부를 지원합니다.")
    @Parameters({
            @Parameter(name = "user", description = "현재 로그인된 사용자 정보 (Spring Security에서 주입)", hidden = true),
            @Parameter(name = "boardReqDto", description = "게시글의 제목과 내용을 포함하는 요청 DTO", required = true,
                    schema = @Schema(implementation = BoardReqDto.class)),
            @Parameter(name = "bindingResult", description = "유효성 검사 결과", hidden = true),
            @Parameter(name = "boardImage", description = "게시글에 첨부할 이미지 파일 (선택 사항)", required = false,
                    content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
    })
    public String registBoard(
            @AuthenticationPrincipal MyUserDetails user,
            @Validated(ValidationGroups.OnCreate.class) @ModelAttribute BoardReqDto boardReqDto,
            BindingResult bindingResult) {

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
            boardService.registBoard(boardReqDto, user.getUser());
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
    @Parameters({
            @Parameter(name = "category", description = "조회할 게시글의 카테고리 (선택 사항). 예: 'community', 'qna'", required = false, example = "community"),
            @Parameter(name = "page", description = "조회할 페이지 번호 (기본값: 0, 1부터 요청 시 내부적으로 0으로 변환)", example = "1"),
            @Parameter(name = "size", description = "한 페이지에 보여줄 게시글 개수 (기본값: 6)", example = "6"),
            @Parameter(name = "sort", description = "정렬 기준 (예: id, createdAt). 기본값은 id,desc", example = "id,desc", hidden = true),
            @Parameter(name = "model", description = "View로 데이터를 전달하기 위한 Spring Model 객체", hidden = true)
    })
    public String boardList(
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
    @Parameters({
            @Parameter(name = "id", description = "조회할 게시글의 ID", required = true, example = "1"),
            @Parameter(name = "model", description = "View로 데이터를 전달하기 위한 Spring Model 객체", hidden = true)
    })
    public String getBoardDetail (
            @PathVariable("id") Long id,
            @AuthenticationPrincipal MyUserDetails user,
            HttpServletRequest request,
            HttpServletResponse response,
            Model model) {

        if (id == null || id <= 0) {
            log.warn("유효하지 않은 게시글 ID로 상세 조회 시도: {}", id);
            throw new CustomException(ErrorStatus._BAD_REQUEST);
        }

        String userIdOrIp;

        if (user != null) {
            userIdOrIp = "user:" + user.getUser().getId();
        } else {
            userIdOrIp = "ip:" + requestParserUtil.getClientIp(request);
        }
        BoardRespDto board = boardService.findBoardById(id, userIdOrIp);
        log.info("board: {}", board);
        if (board == null) {
            log.warn("존재하지 않는 게시글 ID로 상세 조회 시도: {}", id);
            throw new CustomException(ErrorStatus._DATA_NOT_FOUND);
        }
        CookieUtil.updateRecentViewedItemsCookie(id, RECENT_VIEWED_BOARDS, MAX_RECENT_BOARDS, request, response);
        model.addAttribute("board", board);
        return "/board/detail";
    }

    @PostMapping("/delete/{boardId}")
    @Operation(summary = "게시글 삭제", description = "지정된 ID의 게시글을 삭제합니다. 게시글 작성자만 삭제할 수 있습니다.",
            method = "POST")
    @Parameters({
            @Parameter(name = "boardId", description = "삭제할 게시글의 ID", required = true, example = "1"),
            @Parameter(name = "user", description = "현재 로그인된 사용자 정보 (Spring Security에서 주입)", hidden = true)
    })
    public String deleteBoard(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal MyUserDetails user) {
        boardService.deleteBoard(boardId, user.getUser().getId());
        return "redirect:/board/list";
    }

    @PostMapping("/like/{boardId}")
    @Operation(summary = "게시글 좋아요/취소", description = "지정된 게시글에 좋아요를 추가하거나 취소합니다. 로그인된 사용자만 가능합니다.",
            method = "POST")
    @Parameters({
            @Parameter(name = "boardId", description = "좋아요를 누를 게시글의 ID", required = true, example = "1"),
            @Parameter(name = "user", description = "현재 로그인된 사용자 정보 (Spring Security에서 주입)", hidden = true)
    })
    @ResponseBody
    public ResponseEntity<noodlezip.common.dto.ApiResponse<Object>> toggleLike(@PathVariable("boardId") Long boardId, @AuthenticationPrincipal MyUserDetails user) {

        if (user == null || user.getUser() == null) {
            return noodlezip.common.dto.ApiResponse.onFailure(ErrorStatus._UNAUTHORIZED);
        }

        Long userId = user.getUser().getId();
        boolean isLiked = boardService.toggleLike(BoardUserId.builder().userId(userId).communityId(boardId).build());

        long totalLikes = boardService.getLikeCount(boardId);
        LikeResponseDto response = LikeResponseDto.builder().isLiked(isLiked).totalLikes(totalLikes).build();

        return noodlezip.common.dto.ApiResponse.onSuccess(BoardSuccessStatus._OK_LIKED_CHANGED, response);
    }

    @GetMapping("/recent")
    @ResponseBody
    @Operation(summary = "최근 본 게시글 목록 조회", description = "사용자 쿠키에 저장된 최근 본 게시글 ID들을 기반으로 게시글 목록을 조회하여 반환합니다.")
    public ResponseEntity<?> getRecentViewedBoards(HttpServletRequest request) {
        List<Long> recentBoardIds = CookieUtil.getRecentViewedItemIds(request, RECENT_VIEWED_BOARDS);
        return noodlezip.common.dto.ApiResponse.onSuccess(BoardSuccessStatus._OK_GET_BOARD, boardService.getBoardsByIds(recentBoardIds));
    }

    @PostMapping(value = "/imageUpload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    @Operation(summary = "이미지 업로드", description = "게시글 작성 시 사용되는 이미지 파일을 서버에 업로드하고, 업로드된 이미지들의 URL 목록을 반환합니다. 다중 파일 업로드를 지원합니다.")
    public ResponseEntity<?> uploadImage(@RequestParam("uploadFiles") List<MultipartFile> uploadFiles) {
        return noodlezip.common.dto.ApiResponse.onSuccess(BoardSuccessStatus._OK_PHOTO_ADDED, boardService.uploadImages(uploadFiles));
    }

}