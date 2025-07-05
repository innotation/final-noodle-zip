package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;

import java.util.List;

public interface SavedStoreCategoryService {

    void updateSavedCategoryList(Long userId, List<SavedStoreCategoryUpdateRequest> categoryRequests);

    void deleteSavedCategoryList(Long userId, List<Long> categoryIdList);

}
