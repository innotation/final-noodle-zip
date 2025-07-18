package noodlezip.store.controller;

import noodlezip.common.auth.MyUserDetails;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.RamenSoupResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.service.RamenService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RequestMapping("/store")
@Controller
public class StoreRegistController {

    private final StoreService storeService;

    private final RamenService ramenService;

    // 등록 폼 페이지 진입
    @GetMapping("/regist")
    public String showRegistPage(Model model) {
        StoreRequestDto dto = new StoreRequestDto();

        dto.setMenus(new ArrayList<>());
        dto.setExtraToppings(new ArrayList<>());
        dto.setWeekSchedule(new ArrayList<>());

        model.addAttribute("storeRequestDto", dto);
        model.addAttribute("categories", ramenService.getAllCategories());
        model.addAttribute("toppings", ramenService.getAllToppings());
        model.addAttribute("soups", ramenService.getAllSoups());

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

        Long storeId = storeService.registerStore(dto, user);
        return Map.of("storeId", storeId);
    }

}
