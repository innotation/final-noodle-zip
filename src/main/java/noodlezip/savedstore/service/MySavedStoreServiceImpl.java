package noodlezip.savedstore.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.MySavedStorePageResponse;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.dto.response.SavedStoreListWithPageInfoResponse;
import noodlezip.savedstore.dto.response.SavedStorePageResponse;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
import noodlezip.user.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MySavedStoreServiceImpl implements MySavedStoreService {

    private final UserService userService;
    private final SavedStoreService savedStoreService;


    @Override
    @Transactional(readOnly = true)
    public MySavedStorePageResponse getMySavedStoreInitPage(Long userId, SavedStoreCategoryFilterRequest filter) {
        List<SavedStoreCategoryResponse> searchFilter =
                savedStoreService.getSaveCategoryListForSearch(userId, true);
        List<SavedStoreCategoryResponse> updateCategoryList =
                savedStoreService.getSavedCategoryList(userId);
        SavedStoreListWithPageInfoResponse savedStoreList =
                savedStoreService.getSavedStoreListWithPaging(
                        userId,
                        filter,
                        1, true
                );

        return MySavedStorePageResponse.builder()
                .searchFilter(searchFilter)
                .updateCategoryList(updateCategoryList)
                .savedStoreList(savedStoreList)
                .build();
    }


    @Override
    @Transactional(readOnly = true)
    public SavedStorePageResponse getSavedStoreInitPage(Long userId, SavedStoreCategoryFilterRequest filter) {
        userService.findExistingUserByUserId(userId)
                .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_SAVED_STORE_PAGE_LOAD));

        List<SavedStoreCategoryResponse> searchFilter =
                savedStoreService.getSaveCategoryListForSearch(userId, false);
        SavedStoreListWithPageInfoResponse savedStoreList =
                savedStoreService.getSavedStoreListWithPaging(
                        userId,
                        filter,
                        1, false
                );

        return SavedStorePageResponse.builder()
                .searchFilter(searchFilter)
                .savedStoreList(savedStoreList)
                .build();
    }

}
