package noodlezip.community.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.entity.Board;
import noodlezip.community.service.BoardService;
import noodlezip.common.auth.MyUserDetails;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RequestMapping("/board")
@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;
    private final ModelMapper modelMapper;

    @GetMapping("/review")
    public String review() {
        return "/board/leave-review";
    }

    @GetMapping("/registBoard")
    public void registBoard() {}

    @PostMapping("/regist")
    public String regist(@AuthenticationPrincipal MyUserDetails user
                        , BoardReqDto boardReqDto
                        ,@RequestParam("boardImage") MultipartFile boardImage) {
        log.info("regist board : {}", boardReqDto);
        Board board = modelMapper.map(boardReqDto, Board.class);
//        board.setLikesCount(0);
        board.setCommunityType("community");
//        board.setPostStatus();
//        board.setReviewStoreId();
        board.setUserId(user.getUser().getId());
//        board.setViewsCount();
        boardService.registBoard(board, boardImage);
        return "redirect:/board/list";
    }

    @GetMapping("/list")
    public String list(@PageableDefault Pageable pageable, Model model) {
        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1);

        if(pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(pageable.getPageNumber()
                                    ,pageable.getPageSize()
                                    , Sort.by(Sort.Direction.ASC, "id"));
        }

        Map<String, Object> map = boardService.findBoardList(pageable);

        model.addAttribute("board", map.get("list"));
        model.addAttribute("page", map.get("page"));
        model.addAttribute("beginPage", map.get("beginPage"));
        model.addAttribute("endPage", map.get("endPage"));
        model.addAttribute("isFirst", map.get("isFirst"));
        model.addAttribute("isLast", map.get("isLast"));

        return "/board/list";
    }

    @GetMapping("/{id}")
    public String board(@PathVariable Long id, Model model) {
        Board board = boardService.findBoardById(id);
        model.addAttribute("board", board);
        return "/board/detail";
    }

}
