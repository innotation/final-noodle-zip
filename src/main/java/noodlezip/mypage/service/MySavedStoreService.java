package noodlezip.mypage.service;

import noodlezip.mypage.dto.response.savedstore.MySavedStorePageResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStorePageResponse;

public interface MySavedStoreService {

    MySavedStorePageResponse getMySavedStoreInitPage(Long userId);

    SavedStorePageResponse getSavedStoreInitPage(Long userId);

}
