package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreMapResponse;

public interface SavedStoreLocationService {

    SavedStoreMapResponse getStoreLocationList(Long userId,
                                               SavedStoreCategoryFilterRequest filter,
                                               boolean isOwner);

}
