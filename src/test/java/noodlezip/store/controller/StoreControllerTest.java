package noodlezip.store.controller;

import jakarta.transaction.Transactional;
import noodlezip.store.dto.StoreDto;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.service.StoreService;
import noodlezip.store.status.ApprovalStatus;
import noodlezip.store.status.OperationStatus;
import noodlezip.store.status.ParkingType;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
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

//    @TestConfiguration
//    static class MockServiceConfig {
//        @Bean
//        public StoreService storeService() {
//            StoreService mock = Mockito.mock(StoreService.class);
//
//            StoreDto dummyStore = StoreDto.builder()
//                    .id(1L)
//                    .userId(1L)
//                    .storeName("테스트 라멘집")
//                    .address("서울시 테스트구")
//                    .phone("010-1010-0101")
//                    .isLocalCard(false)
//                    .isChildAllowed(true)
//                    .hasParking("주차가능")
//                    .operationStatus("운영중")
//                    .ownerComment("테스트 라면가게입니다.")
//                    .storeMainImageUrl("TESTImageUrl")
//                    .storeLegalCode(110101101)
//                    .approvalStatus("APPROVED")
//                    .build();
//            when(mock.getStore(1L)).thenReturn(dummyStore);
//            return mock;
//        }
//    }

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
}