package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.response.MySavedStorePageResponse;
import noodlezip.savedstore.dto.response.SavedStorePageResponse;

public interface MySavedStoreService {

    MySavedStorePageResponse getMySavedStoreInitPage(Long userId);

    SavedStorePageResponse getSavedStoreInitPage(Long userId);

}
