package noodlezip.store.controller;

import jakarta.transaction.Transactional;
import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.service.StoreService;
import noodlezip.store.status.ApprovalStatus;
import noodlezip.store.status.OperationStatus;
import noodlezip.store.status.ParkingType;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StoreRepository storeRepository;

    @MockBean
    private StoreService storeService;

    @Test
    void 매장상세페이지_정상조회_성공() throws Exception {
        // given: 테스트용 매장 저장
        Store saved = storeRepository.save(Store.builder()
                .userId(1L)
                .storeName("테스트 라멘집")
                .address("서울시 테스트구")
                .phone("010-1010-0101")
                .isLocalCard(false)
                .isChildAllowed(true)
                .hasParking(ParkingType.FREE)
                .operationStatus(OperationStatus.OPEN)
                .ownerComment("테스트 라면가게입니다.")
                .storeMainImageUrl("TESTImageUrl")
                .storeLegalCode(110101101)
                .approvalStatus(ApprovalStatus.APPROVED)
                .storeLat(37.5665)
                .storeLng(126.9780)
                .build());

        // when & then: 상세페이지 호출 후 응답 확인
        mockMvc.perform(get("/store/detail.page")
                        .param("no", String.valueOf(saved.getId())))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("store"))
                .andExpect(view().name("store/detail"))
                .andDo(print());
    }

    @Test
    void testShowDetailMenuList() throws Exception {
        // given
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

        // when & then
        mockMvc.perform(get("/store/detail/menuList")
                        .param("storeId", storeId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].menuName").value("쇼유라멘"))
                .andExpect(jsonPath("$[0].toppingNames[0]").value("계란"))
                .andExpect(jsonPath("$[0].toppingNames[1]").value("차슈"))
                .andDo(print());
    }
}