package noodlezip.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.admin.dto.RegistListDto;
import noodlezip.common.util.PageUtil;
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
    private final PageUtil pageUtil;

    @GetMapping("/main")
    public void mainPage(){}

    @GetMapping("/reportList")
    public String reportList() {
        return "admin/reportList";
    }

    @GetMapping("/registList")
    public String registListPage(@PageableDefault(size = 5) Pageable pageable, Model model) {
        Map<String, Object> page = storeService.findWaitingStores(pageable);

        model.addAttribute("totalCount", page.get("totalCount"));
        model.addAttribute("page", page.get("page"));
        model.addAttribute("size", page.get("size"));
        model.addAttribute("pagePerBlock", page.get("pagePerBlock"));
        model.addAttribute("totalPage", page.get("totalPage"));
        model.addAttribute("beginPage", page.get("beginPage"));
        model.addAttribute("endPage", page.get("endPage"));
        model.addAttribute("isFirst", page.get("isFirst"));
        model.addAttribute("isLast", page.get("isLast"));
        model.addAttribute("registList", page.get("registList"));


        return "admin/registList";
    }

    @GetMapping("/registList/data")
    @ResponseBody
    public Map<String, Object> registListData(@PageableDefault(size = 5) Pageable pageable) {
        return storeService.findWaitingStores(pageable);
    }

    @GetMapping("/recommendList")
    public void recommendList(){}


}
