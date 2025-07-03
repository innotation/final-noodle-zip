package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import noodlezip.user.repository.UserRepository;
import noodlezip.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
public class StoreController {

    private final StoreService storeService;
    private final UserRepository userRepository;

    // 등록 폼 페이지 진입
    @GetMapping("/regist")
    public String showRegistPage(Model model) {
        // 카테고리와 토핑 목록을 서비스에서 가져와서 model에 담기
        model.addAttribute("categories", storeService.getRamenCategories());
        model.addAttribute("toppings", storeService.getRamenToppings());

        return "store/regist";
    }

    @PostMapping("/regist")
    public String registerStore(@ModelAttribute StoreRequestDto dto,
                                @RequestParam("storeMainImage") MultipartFile storeMainImage) throws IOException {
        // 로그인 ID 얻기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loginId = authentication.getName();

        // loginId로 user 직접 조회 → userPk 얻기
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new IllegalArgumentException("로그인한 유저를 찾을 수 없습니다."));
        Long userPk = user.getId();

        // Store 등록
        Long storeId = storeService.registerStore(dto, userPk, storeMainImage);
        return "redirect:/store/detail/" + storeId;
    }
}