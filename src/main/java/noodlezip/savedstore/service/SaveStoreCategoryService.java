package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;

import java.util.List;

public interface SaveStoreCategoryService {

    void updateSavedCategoryList(List<SavedStoreCategoryUpdateRequest> categoryRequests);

    void deleteSavedCategoryList(List<Long> categoryIdList);

}
