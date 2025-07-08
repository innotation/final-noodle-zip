package noodlezip.store.controller;

import noodlezip.common.auth.MyUserDetails;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import noodlezip.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
public class StoreRegistController {

    private final StoreService storeService;

    // 등록 폼 페이지 진입
    @GetMapping("/regist")
    public String showRegistPage(Model model) {

        // 카테고리와 토핑, 육수 목록을 서비스에서 가져와서 model에 담기
        model.addAttribute("toppings", List.of());
        model.addAttribute("categories", List.of());
        model.addAttribute("soups", List.of());

        return "store/regist";
    }

    @PostMapping("/regist")
    public String registerStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                @ModelAttribute StoreRequestDto dto) {
        // 로그인한 유저 객체 얻기
        User user = myUserDetails.getUser();

        // 가게 등록 처리
        Long storeId = storeService.registerStore(dto, dto.getStoreMainImage(), user);
        return "redirect:/store/detail.page?no=" + storeId;
    }
}
