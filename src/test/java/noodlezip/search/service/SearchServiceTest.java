package noodlezip.search.service;

import noodlezip.search.dto.SearchFilterDto;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.store.repository.StoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.*;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchServiceTest {

    @Mock
    private StoreRepository storeRepository;

    @InjectMocks
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Nested
    @DisplayName("getPageLocation")
    class GetPageLocationTest {
        @Test
        void 정상_조회() {
            Pageable pageable = PageRequest.of(0, 10);
            List<SearchStoreDto> content = List.of(new SearchStoreDto());
            Page<SearchStoreDto> page = new PageImpl<>(content, pageable, 1);

            when(storeRepository.findStoreOrderByDistance(37.0, 127.0, pageable)).thenReturn(page);

            Page<SearchStoreDto> result = searchService.getPageLocation(37.0, 127.0, pageable);

            assertThat(result.getContent()).hasSize(1);
            verify(storeRepository).findStoreOrderByDistance(37.0, 127.0, pageable);
        }

        @Test
        void 파라미터_오류_위도범위() {
            Pageable pageable = PageRequest.of(0, 10);
            assertThatThrownBy(() -> searchService.getPageLocation(100.0, 127.0, pageable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 파라미터_오류_pageable_null() {
            assertThatThrownBy(() -> searchService.getPageLocation(37.0, 127.0, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void DB_오류_발생() {
            Pageable pageable = PageRequest.of(0, 10);
            when(storeRepository.findStoreOrderByDistance(anyDouble(), anyDouble(), any()))
                    .thenThrow(new DataAccessException("DB Error") {});

            assertThatThrownBy(() -> searchService.getPageLocation(37.0, 127.0, pageable))
                    .isInstanceOf(DataAccessException.class);
        }
    }

    @Nested
    @DisplayName("searchStoresByFilter")
    class SearchStoresByFilterTest {
        @Test
        void 정상_조회() {
            Pageable pageable = PageRequest.of(0, 10);
            SearchFilterDto filter = new SearchFilterDto();
            List<SearchStoreDto> content = List.of(new SearchStoreDto());
            Page<SearchStoreDto> page = new PageImpl<>(content, pageable, 1);

            when(storeRepository.searchStoresByFilter(filter, pageable)).thenReturn(page);

            Page<SearchStoreDto> result = searchService.searchStoresByFilter(filter, pageable);

            assertThat(result.getContent()).hasSize(1);
            verify(storeRepository).searchStoresByFilter(filter, pageable);
        }

        @Test
        void 파라미터_오류_filter_null() {
            Pageable pageable = PageRequest.of(0, 10);
            assertThatThrownBy(() -> searchService.searchStoresByFilter(null, pageable))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void 파라미터_오류_pageable_null() {
            SearchFilterDto filter = new SearchFilterDto();
            assertThatThrownBy(() -> searchService.searchStoresByFilter(filter, null))
                    .isInstanceOf(IllegalArgumentException.class);
        }

        @Test
        void DB_오류_발생() {
            Pageable pageable = PageRequest.of(0, 10);
            SearchFilterDto filter = new SearchFilterDto();
            when(storeRepository.searchStoresByFilter(any(), any()))
                    .thenThrow(new DataAccessException("DB Error") {});

            assertThatThrownBy(() -> searchService.searchStoresByFilter(filter, pageable))
                    .isInstanceOf(DataAccessException.class);
        }
    }

    @Nested
    @DisplayName("getFilterOptions")
    class GetFilterOptionsTest {
        @Test
        void 정상_조회() {
            when(storeRepository.findDistinctCategories()).thenReturn(List.of("A", "B"));
            when(storeRepository.findDistinctSoups()).thenReturn(List.of("S1"));
            when(storeRepository.findDistinctToppings()).thenReturn(List.of("T1", "T2"));

            Map<String, Object> result = searchService.getFilterOptions();

            assertThat(result.get("categories")).isEqualTo(List.of("A", "B"));
            assertThat(result.get("soups")).isEqualTo(List.of("S1"));
            assertThat(result.get("toppings")).isEqualTo(List.of("T1", "T2"));
        }

        @Test
        void DB_오류_발생() {
            when(storeRepository.findDistinctCategories()).thenThrow(new DataAccessException("DB Error") {});

            assertThatThrownBy(() -> searchService.getFilterOptions())
                    .isInstanceOf(DataAccessException.class);
        }
    }
}