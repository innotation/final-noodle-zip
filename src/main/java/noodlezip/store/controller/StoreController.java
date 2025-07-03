package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import noodlezip.user.repository.UserRepository;
import noodlezip.user.entity.User;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public String registerStore(@AuthenticationPrincipal MyUserDetails myUserDetails,
                                @ModelAttribute StoreRequestDto dto,
                                @RequestParam("storeMainImage") MultipartFile storeMainImage) {
        // 로그인한 유저 객체 얻기
        User user = myUserDetails.getUser();

        // 가게 등록 처리
        Long storeId = storeService.registerStore(dto, storeMainImage, user);

        return "redirect:/store/detail/" + storeId;

    }
}