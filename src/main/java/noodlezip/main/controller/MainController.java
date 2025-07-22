package noodlezip.main.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.community.service.BoardService;
import noodlezip.store.entity.Store;
import noodlezip.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final BoardService boardService;
    private final StoreService storeService;

    @GetMapping("")
    public String index(Model model) {
        boardService.findMostLikedBoardList();
        boardService.findMostViewedBoardList();
        List<Store> popularStores = storeService.getRandomApprovedStores(10);
        model.addAttribute("popularStores", popularStores);
        return "index";
    }
}
