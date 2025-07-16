package noodlezip.store.controller;

import noodlezip.common.util.PageUtil;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.dto.MenuDetailResponseDto;
import noodlezip.store.dto.StoreReviewDto;
import noodlezip.store.service.StoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class StoreControllerUnitTest {

    private MockMvc mockMvc;
    private StoreService storeService;
    private PageUtil pageUtil;
    private RamenService ramenService;

    @BeforeEach
    void setUp() {
        storeService = Mockito.mock(StoreService.class);
        ramenService = Mockito.mock(RamenService.class);

        pageUtil = new PageUtil();
        StoreController storeController = new StoreController(
                storeService,
                pageUtil,
                ramenService
        );  // 생성자 주입
        mockMvc = MockMvcBuilders.standaloneSetup(storeController).build();

        // Pageable을 바인딩해주는 Resolver 등록
        mockMvc = MockMvcBuilders.standaloneSetup(storeController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();
    }

}
