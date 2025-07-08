package noodlezip.savedstore.repository;

import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.SavedStoreResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStoreMapResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SaveStoreQueryRepository {

    Page<SavedStoreResponse> findSavedStoreByCategoryWithPaging(Long userId,
                                                                SavedStoreCategoryFilterRequest filter,
                                                                boolean isOwner,
                                                                Pageable pageable);

    SavedStoreMapResponse getStoreLocationList(Long userId,
                                               List<Long> categoryIdList,
                                               boolean isOwner);

}
