package noodlezip.mypage.service;

import lombok.RequiredArgsConstructor;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.MySavedStorePageResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStoreListResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStorePageResponse;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.service.SavedStoreService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MySavedStoreServiceImpl implements MySavedStoreService {

    private final SavedStoreService savedStoreService;

    @Override
    @Transactional(readOnly = true)
    public MySavedStorePageResponse getMySavedStoreInitPage(Long userId) {
        List<SavedStoreCategoryResponse> searchFilter =
                savedStoreService.getSaveCategoryListForSearch(userId, true);
        List<SavedStoreCategoryResponse> updateCategoryList =
                savedStoreService.getSavedCategoryList(userId);
        SavedStoreListResponse savedStoreList =
                savedStoreService.getSavedStoreListWithPaging(
                        userId,
                        SavedStoreCategoryFilterRequest.builder()
                                .isAllCategory(true)
                                .build(),
                        1, true
                );
//        searchFilter.get(0).setActive(true);

        return MySavedStorePageResponse.builder()
                .searchFilter(searchFilter)
                .updateCategoryList(updateCategoryList)
                .savedStoreList(savedStoreList)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public SavedStorePageResponse getSavedStoreInitPage(Long userId) {
        List<SavedStoreCategoryResponse> searchFilter =
                savedStoreService.getSaveCategoryListForSearch(userId, false);
        SavedStoreListResponse savedStoreList =
                savedStoreService.getSavedStoreListWithPaging(
                        userId,
                        SavedStoreCategoryFilterRequest.builder()
                                .isAllCategory(true)
                                .build(),
                        1, false
                );
//        searchFilter.get(0).setActive(true);

        return SavedStorePageResponse.builder()
                .searchFilter(searchFilter)
                .savedStoreList(savedStoreList)
                .build();
    }

}
