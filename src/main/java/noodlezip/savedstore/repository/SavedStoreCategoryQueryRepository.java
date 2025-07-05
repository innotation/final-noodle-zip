package noodlezip.savedstore.repository;

import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;

import java.util.List;

public interface SavedStoreCategoryQueryRepository {

    List<SavedStoreCategoryResponse> findUserSaveCategoryListForSearch(long userId, boolean isOwner);

    List<SavedStoreCategoryResponse> findUserSavedCategoryList(long userId);

}
