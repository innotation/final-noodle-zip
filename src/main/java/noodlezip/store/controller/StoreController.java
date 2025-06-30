package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 등록 폼 페이지 진입
    @GetMapping("/regist")
    public String showRegistPage(Model model) {
        // 카테고리, 토핑 등 필요한 데이터 세팅 (생략 가능)
        return "store/regist";
    }

    @PostMapping("/regist")
    public String registerStore(@ModelAttribute StoreRequestDto dto,
                                @RequestParam("storeMainImage") MultipartFile storeMainImage) throws IOException {
        Long userId = 1L; // TODO: 실제 로그인 유저 ID로 변경
        Long storeId = storeService.registerStore(dto, userId, storeMainImage);
        return "redirect:/store/detail/" + storeId;
    }
}
