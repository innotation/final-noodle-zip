package noodlezip.savedstore.service;

import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.SavedStoreMapResponse;

public interface SavedStoreLocationService {

    SavedStoreMapResponse getStoreLocationList(Long userId,
                                               SavedStoreCategoryFilterRequest filter,
                                               boolean isOwner);

}
