package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreMapResponse;
import noodlezip.savedstore.dto.response.SavedStoreResponse;
import noodlezip.savedstore.repository.SavedStoreRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SavedStoreLocationServiceImplTest {

    @InjectMocks
    private SavedStoreLocationServiceImpl savedStoreLocationService;

    @Mock
    private SavedStoreRepository savedStoreRepository;

    @Test
    void getStoreLocationList_shouldReturnLocationList() {
        // given
        Long userId = 1L;
        SavedStoreCategoryFilterRequest filter = new SavedStoreCategoryFilterRequest();
        filter.setAllCategory(false);
        filter.setCategoryIdList(List.of(100L, 200L));

        boolean isOwner = true;

        Map<Long, List<SavedStoreResponse>> dummyMap = new HashMap<>();
        dummyMap.put(100L, List.of(dummyResponse(1L, 37.1, 127.2)));
        dummyMap.put(200L, List.of(dummyResponse(2L, 37.2, 127.3)));

        SavedStoreMapResponse mockResponse = new SavedStoreMapResponse();
        mockResponse.setLocationListByCategoryId(dummyMap);

        when(savedStoreRepository.getStoreLocationList(userId, filter, isOwner))
                .thenReturn(mockResponse);

        // when
        SavedStoreMapResponse result = savedStoreLocationService.getStoreLocationList(userId, filter, isOwner);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getLocationListByCategoryId()).containsKey(100L);
        assertThat(result.getLocationListByCategoryId().get(100L)).hasSize(1);
        assertThat(result.getLocationListByCategoryId().get(100L).get(0).getStoreLat()).isEqualTo(37.1);

        verify(savedStoreRepository).getStoreLocationList(userId, filter, isOwner);
    }

    private SavedStoreResponse dummyResponse(Long storeId, double lat, double lng) {
        SavedStoreResponse response = new SavedStoreResponse();
        response.setSavedStoreId(storeId);
        response.setStoreLat(lat);
        response.setStoreLng(lng);
        return response;
    }

}