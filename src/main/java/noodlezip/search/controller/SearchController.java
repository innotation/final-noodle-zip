package noodlezip.search.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.search.service.SearchService;
import noodlezip.store.dto.StoreDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/search")
@Controller
public class SearchController {

    private final SearchService searchService;

    @GetMapping("/list")
    public void searchStore(Model model){
        List<StoreDto> stores = searchService.findAllStore();
        model.addAttribute(stores);
    }

}
