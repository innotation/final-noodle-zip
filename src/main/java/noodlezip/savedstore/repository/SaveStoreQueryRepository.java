package noodlezip.savedstore.repository;

import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreResponse;
import noodlezip.savedstore.dto.response.SavedStoreMapResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface SaveStoreQueryRepository {

    Page<SavedStoreResponse> findSavedStoreByCategoryWithPaging(Long userId,
                                                                SavedStoreCategoryFilterRequest filter,
                                                                boolean isOwner,
                                                                Pageable pageable);

    SavedStoreMapResponse getStoreLocationList(Long userId,
                                               SavedStoreCategoryFilterRequest filter,
                                               boolean isOwner);

}
