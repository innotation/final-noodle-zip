package noodlezip.ramen.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.Category;
import noodlezip.ramen.entity.Topping;
import noodlezip.ramen.repository.CategoryRepository;
import noodlezip.ramen.repository.RamenSoupRepository;
import noodlezip.ramen.repository.ToppingRepository;
import org.springframework.stereotype.Service;
import noodlezip.ramen.dto.RamenSoupResponseDto;
import noodlezip.ramen.entity.RamenSoup;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RamenService {

    private final ToppingRepository toppingRepository;
    private final CategoryRepository categoryRepository;
    private final RamenSoupRepository ramenSoupRepository;

    // 전체 토핑 목록 조회
    public List<ToppingResponseDto> getAllToppings() {
        List<Topping> toppings = toppingRepository.findAll();
        return toppings.stream()
                .map(t -> new ToppingResponseDto(t.getId(), t.getToppingName()))
                .collect(Collectors.toList());
    }

    // 전체 카테고리 목록 조회
    public List<CategoryResponseDto> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(c -> new CategoryResponseDto(c.getId(), c.getCategoryName()))
                .collect(Collectors.toList());
    }

    // 전체 육수 목록 조회
    public List<RamenSoupResponseDto> getAllSoups() {
        List<RamenSoup> soups = ramenSoupRepository.findAll();
        return soups.stream()
                .map(s -> new RamenSoupResponseDto(s.getId(), s.getSoupName()))
                .collect(Collectors.toList());
    }

}
