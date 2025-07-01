package noodlezip.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.admin.dto.RegistListDto;
import noodlezip.store.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    private final StoreService storeService;
    private final noodlezip.util.PageUtil pageUtil;

    @GetMapping("/main")
    public void mainPage(){}

    @GetMapping("/reportList")
    public String reportList(@PageableDefault(size = 5) Pageable pageable, Model model) {

        // 페이지 번호 보정: 0부터 시작 → 1부터로 보정
        pageable = pageable.withPage(Math.max(0, pageable.getPageNumber() - 1));

        // 기본 정렬이 없는 경우 created_at DESC 정렬 적용
        if (pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("created_at").descending()
            );
        }

        // 서비스로부터 Map 반환받기
        Map<String, Object> result = storeService.findRegistList(pageable);

        // 모델에 데이터 전달
        model.addAttribute("registList", result.get("registList"));
        model.addAttribute("page", result.get("page"));
        model.addAttribute("beginPage", result.get("beginPage"));
        model.addAttribute("endPage", result.get("endPage"));
        model.addAttribute("isFirst", result.get("isFirst"));
        model.addAttribute("isLast", result.get("isLast"));

        return "admin/reportList";
    }

    @GetMapping("/registList")
    public String registList(@PageableDefault(size = 5) Pageable pageable, Model model) {

        // 페이지 번호 보정: 0부터 시작 → 1부터로 보정
        pageable = pageable.withPage(Math.max(0, pageable.getPageNumber() - 1));

        // 기본 정렬이 없는 경우 created_at DESC 정렬 적용
        if (pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(
                    pageable.getPageNumber(),
                    pageable.getPageSize(),
                    Sort.by("created_at").descending()
            );
        }

        // 서비스로부터 Map 반환받기
        Map<String, Object> result = storeService.findRegistList(pageable);

        // 모델에 데이터 전달
        model.addAttribute("registList", result.get("registList"));
        model.addAttribute("page", result.get("page"));
        model.addAttribute("beginPage", result.get("beginPage"));
        model.addAttribute("endPage", result.get("endPage"));
        model.addAttribute("isFirst", result.get("isFirst"));
        model.addAttribute("isLast", result.get("isLast"));

        return "admin/registList";
    }

    @GetMapping("/recommendList")
    public void recommendList(){}


}
