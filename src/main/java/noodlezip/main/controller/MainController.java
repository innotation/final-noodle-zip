package noodlezip.main.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.community.service.BoardService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;

    @GetMapping("")
    public String index() {
        boardService.findMostLikedBoardList();
        boardService.findMostViewedBoardList();
        return "index";
    }
}
