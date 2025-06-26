package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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

}
