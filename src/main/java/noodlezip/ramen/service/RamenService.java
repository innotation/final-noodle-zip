package noodlezip.ramen.service;

import lombok.RequiredArgsConstructor;
import noodlezip.ramen.dto.CategoryResponseDto;
import noodlezip.ramen.dto.RamenSoupResponseDto;
import noodlezip.ramen.dto.ToppingResponseDto;
import noodlezip.ramen.entity.Category;
import noodlezip.ramen.entity.RamenSoup;
import noodlezip.ramen.entity.Topping;
import noodlezip.ramen.repository.CategoryRepository;
import noodlezip.ramen.repository.RamenReviewRepository;
import noodlezip.ramen.repository.RamenSoupRepository;
import noodlezip.ramen.repository.ToppingRepository;
import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.dto.ReviewSummaryDto;
import noodlezip.store.dto.StoreReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RamenService {

    private final ToppingRepository toppingRepository;
    private final CategoryRepository categoryRepository;
    private final RamenReviewRepository ramenReviewRepository;
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

    // 현재 매장의 메뉴가 포함한 카테고리 조회
    public List<String> extractUniqueCategories(List<MenuDetailDto> menus) {
        return menus.stream()
                .map(MenuDetailDto::getCategoryName)
                .distinct()
                .collect(Collectors.toList());
    }

    // 현재 매장의 메뉴가 포함한 육수 조회
    public List<String> extractUniqueSoups(List<MenuDetailDto> menus) {
        return menus.stream()
                .map(MenuDetailDto::getSoupName)
                .distinct()
                .collect(Collectors.toList());
    }

    // 현재 매장의 메뉴가 포함한 토핑 조회
    public List<String> extractUniqueToppings(List<MenuDetailDto> menus) {
        return menus.stream()
                .flatMap(menu -> menu.getToppingNames().stream())
                .distinct()
                .collect(Collectors.toList());
    }

    public ReviewSummaryDto getSummaryByStoreId(Long storeId) {
        return ramenReviewRepository.getSummaryByStoreId(storeId);
    }

    public ReviewSummaryDto getSummaryByStoreIdAndMenuName(Long storeId, String menuName) {
        return ramenReviewRepository.getSummaryByStoreIdAndMenuName(storeId, menuName);
    }

    // 전체 육수 목록 조회
    public List<RamenSoupResponseDto> getAllSoups() {
        List<RamenSoup> soups = ramenSoupRepository.findAll();
        return soups.stream()
                .map(s -> new RamenSoupResponseDto(s.getId(), s.getSoupName()))
                .collect(Collectors.toList());
    }

    // 카테고리별 리뷰 개수 조회
    public Map<String, Long> getReviewCountByCategory() {
        return ramenReviewRepository.getReviewCountByCategory();
    }

    // 육수별 리뷰 개수 조회
    public Map<String, Long> getReviewCountBySoup() {
        return ramenReviewRepository.getReviewCountBySoup();
    }

    // 태그로 필터링된 리뷰 목록 조회
    public Page<StoreReviewDto> findReviewsByTag(String tag, String type, Pageable pageable) {
        return ramenReviewRepository.findReviewsByTag(tag, type, pageable);
    }
}

