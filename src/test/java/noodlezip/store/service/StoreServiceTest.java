package noodlezip.store.service;

import jakarta.persistence.EntityManager;
import noodlezip.common.util.PageUtil;
import noodlezip.ramen.repository.RamenToppingRepository;
import noodlezip.ramen.repository.ToppingRepository;
import noodlezip.ramen.service.RamenService;
import noodlezip.store.dto.MenuDetailDto;
import noodlezip.store.repository.MenuRepository;
import noodlezip.store.repository.StoreRepository;
import noodlezip.store.repository.StoreWeekScheduleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class StoreServiceTest {

    @Mock
    private StoreRepository storeRepository;
    @Mock private StoreWeekScheduleRepository scheduleRepository;
    @Mock private MenuRepository menuRepository;
    @Mock private RamenToppingRepository ramenToppingRepository;
    @Mock private RamenService ramenService;
    @Mock private ModelMapper modelMapper;
    @Mock private PageUtil pageUtil;
    @Mock private ToppingRepository toppingRepository;
    @Mock private EntityManager entityManager;

    private StoreService storeService;

    @BeforeEach
    void setUp() {
        storeService = new StoreService(
                storeRepository,
                scheduleRepository,
                menuRepository,
                ramenToppingRepository,
                ramenService,
                modelMapper,
                pageUtil,
                toppingRepository,
                entityManager
        );
    }

    @Test
    void 매장_메뉴_조회() {
        // given
        Long storeId = 1L;
        List<MenuDetailDto> menus = List.of(
                MenuDetailDto.builder()
                        .menuId(10L)
                        .menuName("쇼유라멘")
                        .build()
        );

        Map<Long, List<String>> toppingMap = Map.of(10L, List.of("계란", "차슈"));

        given(menuRepository.findMenuDetailByStoreId(storeId)).willReturn(menus);
        given(ramenToppingRepository.findToppingNamesByStoreGroupedByMenuId(storeId)).willReturn(toppingMap);

        // when
        List<MenuDetailDto> result = storeService.getMenus(storeId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getToppingNames()).containsExactly("계란", "차슈");
    }
}