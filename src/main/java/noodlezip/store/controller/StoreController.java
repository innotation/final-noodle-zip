package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 등록 폼 페이지 진입
    @GetMapping("/regist")
    public String showRegistPage() {
        return "store/regist"; // templates/store/regist.html
    }

    // 등록 처리
    @PostMapping("/regist")
    public String registerStore(@ModelAttribute StoreRequestDto dto) {
        Long storeId = storeService.registerStore(dto);
        // 등록 후 상세 페이지로 리다이렉트
        return "redirect:/store/detail/" + storeId;
    }

    // 매장 상세페이지 진입
    @GetMapping("/detail.page")
    public String showDetailPage(Long no, Model model) {
        StoreDto store = storeService.getStore(no);
        model.addAttribute("store", store);
        return "store/detail";
    }

}
