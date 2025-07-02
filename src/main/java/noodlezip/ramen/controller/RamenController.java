package noodlezip.ramen.controller;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
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

    private final RamenService ramenService;

    @GetMapping("/categories")
    @ResponseBody
    public List<CategoryResponseDto> getCategories() {
        return ramenService.getAllCategories();
    }

    @GetMapping("/toppings")
    @ResponseBody
    public List<ToppingResponseDto> getToppings() {
        return ramenService.getAllToppings();
    }
}