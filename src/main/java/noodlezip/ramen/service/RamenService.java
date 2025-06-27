package noodlezip.ramen.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.RamenCategory;
import noodlezip.ramen.entity.Topping;
import noodlezip.ramen.repository.RamenCategoryRepository;
import noodlezip.ramen.repository.ToppingRepository;
import noodlezip.ramen.repository.RamenSoupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RamenService {

    private final RamenCategoryRepository ramenCategoryRepository;
    private final RamenSoupRepository ramenSoupRepository;
    private final ToppingRepository toppingRepository; // 변경

    public List<CategoryResponseDto> getAllCategories() {
        List<RamenCategory> categories = ramenCategoryRepository.findAll();
        return categories.stream()
                .map(c -> new CategoryResponseDto(c.getId(), c.getCategoryName()))
                .collect(Collectors.toList());
    }

    public List<ToppingResponseDto> getAllToppings() {
        List<Topping> toppings = toppingRepository.findAll();
        return toppings.stream()
                .map(t -> new ToppingResponseDto(t.getId(), t.getToppingName()))
                .collect(Collectors.toList());
    }
}