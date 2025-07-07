package noodlezip.store.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.common.util.PageUtil;
import noodlezip.store.dto.*;
import noodlezip.store.service.StoreService;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
public class StoreController {

    private final StoreService storeService;
    private final UserRepository userRepository;
    private final PageUtil pageUtil;

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
                                @ModelAttribute StoreRequestDto dto) {
        // 로그인한 유저 객체 얻기
        User user = myUserDetails.getUser();
        log.info("Store registration requested by userId: {}, storeName: {}", user.getId(), dto.getStoreName());

        for (MenuRequestDto menu : dto.getMenus()) {
            log.info("메뉴명: {}", menu.getMenuName());
            log.info(" - 가격: {}", menu.getPrice());
            log.info(" - 기본 토핑: {}", menu.getDefaultToppingIds());
            log.info(" - 추가 토핑: {}", menu.getExtraToppings());
            log.info(" - 이미지 파일명: {}",
                    menu.getMenuImageFile() != null ? menu.getMenuImageFile().getOriginalFilename() : "없음");
        }

        // 가게 등록 처리
        Long storeId = storeService.registerStore(dto, dto.getStoreMainImage(), user);

        log.info("StoreRequestDto: {}", dto);

        return "redirect:/store/detail.page?no=" + storeId;
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


    // 매장 리뷰 조회
    @GetMapping("/detail/review")
    @ResponseBody
    public StoreReviewResponseDto showStoreReviewList(
            @RequestParam Long storeId,
            Pageable pageable
    ) {

        Page<StoreReviewDto> reviews = storeService.getReviews(storeId, pageable);
        return StoreReviewResponseDto.builder()
                .reviews(reviews.getContent())
                .pagination(pageUtil.getPageInfo(reviews, 10))
                .build();
    }

}