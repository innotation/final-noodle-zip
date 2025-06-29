package noodlezip.search.controller;

import noodlezip.store.repository.StoreRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

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

    @Test
    void 검색필터_적용_매장조회_성공() throws Exception {
        // given
        String lat = "37.5665";
        String lng = "126.9780";
        String page = "1";
        String size = "10";

        // 예시 필터: 돈코츠, 소유, 계란
        String ramenCategory = "돈코츠";
        String ramenSoup = "소유";
        String topping = "계란";

        // when & then
        mockMvc.perform(get("/search/filter")
                        .param("lat", lat)
                        .param("lng", lng)
                        .param("page", page)
                        .param("size", size)
                        .param("ramenCategory", ramenCategory)
                        .param("ramenSoup", ramenSoup)
                        .param("topping", topping))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(lessThanOrEqualTo(10)))
                .andExpect(jsonPath("$.content[0].storeName").exists())
                .andExpect(jsonPath("$.content[0].distance").isNumber())
                .andDo(print());
    }
}