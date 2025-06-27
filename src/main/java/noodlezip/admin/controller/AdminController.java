package noodlezip.admin.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Controller
public class AdminController {

    @GetMapping("/main")
    public void mainPage(){}

    @GetMapping("/reportList")
    public void reportListPage(){}

    @GetMapping("/registList")
    public void registListPage(@PageableDefault(size =5) Pageable pageable, Model model){
        pageable = pageable.withPage(pageable.getPageNumber() <= 0? 0: pageable.getPageNumber()-1);

        if(pageable.getSort().isEmpty()) {
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("").descending());
        }


    }

    @GetMapping("/recommendList")
    public void recommendList(){}
//    public String menuList(@PageableDefault Pageable pageable, Model model){
//
//        log.info("pageable: {}", pageable); // Pageable 매개변수에 page, size, sort 자동으로 바인딩 됨
//
//        // withPage() : 현재 Pageable의 기존설정(페이지size, 정렬 등)은 그대로 두고, 페이지 번호만 바꾼 "새로운 Pageable 객체를 반환"
//        pageable = pageable.withPage(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber()-1);
//
//        if(pageable.getSort().isEmpty()){ // 정렬 파라미터가 존재하지 않을 경우 => 기본 정렬 기준 세우기
//            // 정렬만 바꾸는건 따로 존재하지 않음 => 다시 새로 생성해야됨
//            pageable = PageRequest.of(pageable.getPageNumber()
//                    , pageable.getPageSize()
//                    , Sort.by("menuCode").descending());
//        }
//
//        log.info("변경후 pageable: {}", pageable);
//
//        Map<String, Object> map = menuService.findMenuList(pageable);
//        model.addAttribute("menuList", map.get("menuList"));
//        model.addAttribute("page", map.get("page"));
//        model.addAttribute("beginPage", map.get("beginPage"));
//        model.addAttribute("endPage", map.get("endPage"));
//        model.addAttribute("isFirst", map.get("isFirst"));
//        model.addAttribute("isLast", map.get("isLast"));
//
//        return "admin/recommendList";

}
