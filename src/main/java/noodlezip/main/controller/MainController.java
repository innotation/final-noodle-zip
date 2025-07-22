package noodlezip.main.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.community.service.BoardService;
import noodlezip.store.entity.Store;
import noodlezip.store.service.StoreService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public String index(
            @AuthenticationPrincipal MyUserDetails userDetails,
            Model model) {
        boardService.findMostLikedBoardList();
        boardService.findMostViewedBoardList();
        List<Store> popularStores = storeService.getRandomApprovedStores(10);
        if (userDetails != null) {
            Long userId = userDetails.getUser().getId();
            List<Store> recommendedStores = storeService.getRecommendedStoresForUser(userId, 10);
            model.addAttribute("recommendedStores", recommendedStores);
        }
        model.addAttribute("popularStores", popularStores);

        return "index";
    }
}
