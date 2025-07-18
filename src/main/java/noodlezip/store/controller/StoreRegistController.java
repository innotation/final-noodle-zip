package noodlezip.store.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import noodlezip.user.entity.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
@Slf4j
public class StoreRegistController {

    private final StoreService storeService;

    // 등록 폼 페이지 진입
    @GetMapping("/regist")
    public String showRegistPage(Model model) {

        // 카테고리와 토핑, 육수 목록을 서비스에서 가져와서 model에 담기
        model.addAttribute("toppings", storeService.getRamenToppings());
        model.addAttribute("categories", storeService.getRamenCategories());
        model.addAttribute("soups", storeService.getAllSoups());

        return "store/regist";
    }

    @PostMapping(value = "/regist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Map<String, Object> registerStore(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @Valid @RequestPart("store") StoreRequestDto dto,
            BindingResult bindingResult,
            @RequestPart(value = "storeMainImage", required = false) MultipartFile storeMainImage,
            @RequestPart(value = "menuImageFiles", required = false) List<MultipartFile> menuImageFiles
    ) {

        // 유효성 검사 실패 시 응답 반환
        if (bindingResult.hasErrors()) {
            String errorMessage = "요청값이 유효하지 않습니다.";

            if (bindingResult.getFieldError() != null) {
                String msg = bindingResult.getFieldError().getDefaultMessage();
                if (msg != null) {
                    errorMessage = msg;
                }
            }

            return Map.of(
                    "isSuccess", false,
                    "code", "VALIDATION_ERROR",
                    "message", errorMessage
            );
        }

        // 유저
        User user = myUserDetails.getUser();

        dto.setStoreMainImage(storeMainImage);           // 대표 이미지 주입
        for (int i = 0; i < dto.getMenus().size(); i++) {
            if (menuImageFiles != null && i < menuImageFiles.size()) {
                dto.getMenus().get(i).setMenuImageFile(menuImageFiles.get(i));  // 메뉴 이미지 주입
            }
        }

        Long storeId = storeService.registerStore(dto, storeMainImage, user);
        return Map.of("storeId", storeId);
    }
}
