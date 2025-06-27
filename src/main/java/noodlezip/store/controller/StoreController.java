package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/store")
@RequiredArgsConstructor
public class StoreController {

    private final StoreService storeService;

    // 등록 폼 페이지 진입
    @GetMapping("/regist")
    public String showRegistPage(Model model) {
        // 라멘 카테고리와 토핑 목록을 가져옴
        List<CategoryResponseDto> categories = storeService.getRamenCategories();
        List<ToppingResponseDto> toppings = storeService.getRamenToppings();

        model.addAttribute("categories", categories);
        model.addAttribute("toppings", toppings);

        return "store/regist"; // templates/store/regist.html
    }

    // 등록 처리
    @PostMapping("/regist")
    public String registerStore(@ModelAttribute StoreRequestDto dto) {
        Long userId = 1L; // TODO: 실제 로그인 유저 ID로 변경 필요
        Long storeId = storeService.registerStore(dto, userId);

        // 등록 후 상세 페이지로 리다이렉트
        return "redirect:/store/detail/" + storeId;
    }

}
