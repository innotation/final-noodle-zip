package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.AddCategoryRequest;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.dto.response.SavedStoreListWithPageInfoResponse;

import java.util.List;

public interface SavedStoreService {

    boolean isSavedStore(Long userId, Long storeId);

    List<SavedStoreCategoryResponse> getUserSaveCategoryList(Long userId, Long storeId);

    String getUserSavedStoreMemo(Long userId, Long storeId);

    void addSavedStoreCategory(Long userId, AddCategoryRequest addCategoryRequest);

    void saveSavedStore(Long userId, Long storeId, SaveStoreRequest saveStoreRequest);

    void deleteSavedStore(Long userId, Long storeId);

    List<SavedStoreCategoryResponse> getSaveCategoryListForSearch(Long userId, boolean isOwner);

    List<SavedStoreCategoryResponse> getSavedCategoryList(Long userId);

    SavedStoreListWithPageInfoResponse getSavedStoreListWithPaging(Long userId,
                                                                   SavedStoreCategoryFilterRequest filter,
                                                                   int page,
                                                                   boolean isOwner);

}
