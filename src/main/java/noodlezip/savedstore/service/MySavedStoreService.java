package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.MySavedStorePageResponse;
import noodlezip.savedstore.dto.response.SavedStorePageResponse;

public interface MySavedStoreService {

    MySavedStorePageResponse getMySavedStoreInitPage(Long userId, SavedStoreCategoryFilterRequest filter);

    SavedStorePageResponse getSavedStoreInitPage(Long userId, SavedStoreCategoryFilterRequest filter);

}
