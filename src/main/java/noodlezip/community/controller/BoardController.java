package noodlezip.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.validation.ValidationGroups;
import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.entity.Board;
import noodlezip.community.service.BoardService;
import noodlezip.common.auth.MyUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/review")
    public String review() {
        return "/board/leave-review";
    }

    @GetMapping("/registBoard")
    public void registBoard() {}

    @PostMapping("/regist")
    public String regist(@AuthenticationPrincipal MyUserDetails user
                        ,@Validated(ValidationGroups.OnCreate.class) BoardReqDto boardReqDto
                        , BindingResult bindingResult
                        ,@RequestParam(value = "boardImage", required = false) MultipartFile boardImage) {
        if (bindingResult.hasErrors()) {
            log.error("게시판 작성 유효성 검사 실패: {}", bindingResult.getAllErrors());
            throw new IllegalArgumentException("게시판 작성 유효성 검사 실패");
        }
        boardService.registBoard(boardReqDto, user.getUser().getId(), boardImage);
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String list(@PageableDefault(size = 6, sort = "id", direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        // 0보다 작을 경우 예외처리 및 인덱싱
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);

        Map<String, Object> map = boardService.findBoardList(pageable);

        model.addAttribute("board", map.get("list"));
        model.addAttribute("page", map.get("page"));
        model.addAttribute("beginPage", map.get("beginPage"));
        model.addAttribute("endPage", map.get("endPage"));
        model.addAttribute("isFirst", map.get("isFirst"));
        model.addAttribute("isLast", map.get("isLast"));

        return "/board/list";
    }

    @GetMapping("/detail/{id}")
    public String board(@PathVariable("id") Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "/board/detail";
    }

}
