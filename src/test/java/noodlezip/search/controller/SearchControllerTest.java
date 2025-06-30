package noodlezip.search.controller;

import jakarta.transaction.Transactional;
import noodlezip.ramen.entity.*;
import noodlezip.ramen.repository.CategoryRepository;
import noodlezip.ramen.repository.RamenSoupRepository;
import noodlezip.ramen.repository.RamenToppingRepository;
import noodlezip.ramen.repository.ToppingRepository;
import noodlezip.store.entity.Menu;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.LocalDateTime;

import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SearchControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private MenuRepository menuRepository;
    @Autowired
    private RamenSoupRepository ramenSoupRepository;
    @Autowired
    private RamenToppingRepository ramenToppingRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ToppingRepository toppingRepository;

    @Transactional
    @Test
    void 검색필터_적용_매장조회_성공() throws Exception {
        // given
        // ─────── 1. 가게 저장 ───────
        Store store = Store.builder()
                .userId(1L)
                .storeName("테스트 라멘집")
                .address("서울시 테스트구")
                .phone("010-1234-5655")
                .isLocalCard(true)
                .isChildAllowed(true)
                .hasParking("있음")
                .operationStatus("운영중")
                .ownerComment("테스트용 가게입니다.")
                .storeMainImageUrl("https://example.com/image.jpg")
                .storeLat(37.5665)
                .storeLng(126.9780)
                .approvalStatus("WAITING")
                .storeLegalCode(12401293)
                .build();
        storeRepository.save(store);

        // ─────── 2. 라멘 카테고리 저장 ───────
        Category category = categoryRepository.save(
                Category.builder()
                        .name("돈코츠")
                        .build()
        );

        // ─────── 3. 라멘 국물 저장 ───────
        RamenSoup soup = ramenSoupRepository.save(
                RamenSoup.builder()
                        .soupName("진한국물")
                        .build()
        );

        // ─────── 4. 메뉴 저장 ───────
        Menu menu = menuRepository.save(
                Menu.builder()
                        .storeId(store.getId())
                        .menuName("기본라멘")
                        .price(9000)
                        .ramenCategoryId(category.getId())
                        .ramenSoupId(soup.getId())
                        .build()
        );

        // ─────── 5. 토핑 저장 ───────
        Topping saveTopping = toppingRepository.save(
                Topping.builder()
                        .toppingName("계란")
                        .isActive(true)
                        .build()
        );

        // ─────── 6. 메뉴-토핑 연결 ───────
        RamenToppingId ramenToppingId = new RamenToppingId(menu.getId(), saveTopping.getId());
        RamenTopping ramentopping = new RamenTopping();
        ramentopping.setToppingId(ramenToppingId);
        ramenToppingRepository.save(ramentopping);

        String lat = "37.5665";
        String lng = "126.9780";
        String page = "1";
        String size = "10";

        // 필터: 돈코츠, 소유, 계란, 강남구
        String ramenCategory = "돈코츠";
        String ramenSoup = "진한국물";
        String topping = "계란";
        String region = "테스트구";

        // when & then
        mockMvc.perform(get("/search/filter")
                        .param("lat", lat)
                        .param("lng", lng)
                        .param("page", page)
                        .param("size", size)
                        .param("ramenCategory", ramenCategory)
                        .param("ramenSoup", ramenSoup)
                        .param("topping", topping)
                        .param("region", region))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(10)))
                .andExpect(jsonPath("$.content[0].storeName").exists())
                .andExpect(jsonPath("$.content[0].distance").isNumber())
                .andDo(print());
    }
}