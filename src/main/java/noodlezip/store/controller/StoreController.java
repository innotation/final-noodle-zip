package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    // 등록 처리
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

    // 매장 상세페이지 진입
    @GetMapping("/detail.page")
    public String showDetailPage(Long no, Model model) {
        StoreDto store = storeService.getStore(no);
        model.addAttribute("store", store);
        return "store/detail";
    }

    // 매장 메뉴 조회 (비동기)
    @GetMapping("/detail/menuList")
    @ResponseBody
    public List<MenuDetailDto> showDetailMenuList(Long storeId) {
        return storeService.getMenus(storeId);
    }

}
