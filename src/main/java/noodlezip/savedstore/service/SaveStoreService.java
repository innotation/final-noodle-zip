package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;

import java.util.List;

public interface SaveStoreService {

    List<SavedStoreCategoryResponse> getUserSaveCategoryList(Long userId, String storeId);

    void addSavedStore(Long userId, Long storeId, SaveStoreRequest saveStoreRequest);

    void deleteSavedStore(Long userId, Long storeId);


    /// 마이페이지에서 검색한 카테고리로 가게정보들 반환

}
