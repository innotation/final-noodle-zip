package noodlezip.store.controller;

import lombok.extern.slf4j.Slf4j;
import noodlezip.common.auth.MyUserDetails;
import noodlezip.store.dto.StoreRequestDto;
import noodlezip.store.service.StoreService;
import noodlezip.user.entity.User;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
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
        model.addAttribute("toppings", List.of());
        model.addAttribute("categories", List.of());
        model.addAttribute("soups", List.of());

        return "store/regist";
    }

    @PostMapping(value = "/regist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseBody
    public Map<String, Object> registerStore(
            @AuthenticationPrincipal MyUserDetails myUserDetails,
            @RequestPart("store") StoreRequestDto dto,
            @RequestPart(value = "storeMainImage", required = false) MultipartFile storeMainImage,
            @RequestPart(value = "menuImageFiles", required = false) List<MultipartFile> menuImageFiles
    ) {
        // 유저
        User user = myUserDetails.getUser();

        dto.setStoreMainImage(storeMainImage);           // 대표 이미지 주입
        for (int i = 0; i < dto.getMenus().size(); i++) {
            if (menuImageFiles != null && i < menuImageFiles.size()) {
                dto.getMenus().get(i).setMenuImageFile(menuImageFiles.get(i));  // 메뉴 이미지 주입
            }
        }

        System.out.println(dto);

        Long storeId = storeService.registerStore(dto, storeMainImage, user);
        return Map.of("storeId", storeId);
    }

}
