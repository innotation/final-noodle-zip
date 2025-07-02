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

    @GetMapping("/main")
    public void mainPage(){}

    @GetMapping("/reportList")
    public String reportList(@PageableDefault(size = 5) Pageable pageable, Model model) {

        return "admin/reportList";
    }

    @GetMapping("/registList")
    public String registListPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Page<RegistListDto> page = storeService.findWaitingStores(pageable);

        model.addAttribute("registList", page.getContent());
        model.addAttribute("page", page.getNumber());           // 현재 페이지 번호
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("isFirst", page.isFirst());
        model.addAttribute("isLast", page.isLast());
        model.addAttribute("beginPage", Math.max(0, page.getNumber() - 2));
        model.addAttribute("endPage", Math.min(page.getTotalPages() - 1, page.getNumber() + 2));

        return "admin/registList";
    }

    @GetMapping("/registList/data")
    @ResponseBody
    public Page<RegistListDto> registListData(@PageableDefault(size = 5) Pageable pageable) {
        return storeService.findWaitingStores(pageable);
    }

    @GetMapping("/recommendList")
    public void recommendList(){}


}
