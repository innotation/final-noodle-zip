package noodlezip.ramen.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.RamenSoupResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.service.RamenService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/ramen")
public class RamenController {

    private final RamenService toppingService;
    private final RamenService categoryService;
    private final RamenService soupService;

    @ResponseBody
    @GetMapping("/toppings")
    public List<ToppingResponseDto> getToppings() {
        return toppingService.getAllToppings();
    }

    @ResponseBody
    @GetMapping("/categories")
    public List<CategoryResponseDto> getCategories() {
        return categoryService.getAllCategories();
    }

    @ResponseBody
    @GetMapping("/soups")
    public List<RamenSoupResponseDto> getSoups() {
        return soupService.getAllSoups();
    }
}