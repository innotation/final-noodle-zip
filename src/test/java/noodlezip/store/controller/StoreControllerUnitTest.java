package noodlezip.store.controller;

import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.service.StoreService;
import noodlezip.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreControllerUnitTest {

    private MockMvc mockMvc;
    private StoreService storeService;
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        storeService = Mockito.mock(StoreService.class);
        userRepository = Mockito.mock(UserRepository.class);
        StoreController storeController = new StoreController(storeService, userRepository);  // 생성자 주입
        mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();
    }

    @Test
    void 메뉴_디테일_테스트() throws Exception {
        Long storeId = 1L;
        List<MenuDetailDto> menuList = List.of(
                MenuDetailDto.builder()
                        .menuId(1L)
                        .menuName("쇼유라멘")
                        .price(9000)
                        .menuDescription("기본 쇼유 국물")
                        .menuImageUrl("https://example.com/image.jpg")
                        .categoryName("쇼유")
                        .soupName("쇼유")
                        .toppingNames(List.of("계란", "차슈"))
                        .build()
        );

        given(storeService.getMenus(storeId)).willReturn(menuList);

        mockMvc.perform(get("/store/detail/menuList").param("storeId", storeId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].menuName").value("쇼유라멘"))
                .andExpect(jsonPath("$[0].toppingNames[0]").value("계란"))
                .andExpect(jsonPath("$[0].toppingNames[1]").value("차슈"));
    }
}
