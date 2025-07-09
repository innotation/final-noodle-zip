package noodlezip.savedstore.service;

import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreListWithPageInfoResponse;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;

import java.util.List;

public interface SavedStoreService {

    /// todo \[가게상세-1] 가게 상세페이지 wishlist 활성화 여부 판단
    boolean isSavedStore(Long userId, Long storeId);

    /// todo \[가게상세-2] wishlist 클릭 후 모달에 띄워질 사용자 가게저장카테고리 리스트
    List<SavedStoreCategoryResponse> getUserSaveCategoryList(Long userId, Long storeId);

    /// todo \[가게상세-3] 가게 저장 및 수정 - 모달 저장 버튼 클릭시
    void saveSavedStore(Long userId, Long storeId, SaveStoreRequest saveStoreRequest);

    /// todo \[가게상세-4] 가게저장 리스트에 삭제 - 모달 '삭제' 버튼 클릭시
    void deleteSavedStore(Long userId, Long storeId);

    List<SavedStoreCategoryResponse> getSaveCategoryListForSearch(Long userId, boolean isOwner);

    List<SavedStoreCategoryResponse> getSavedCategoryList(Long userId);

    SavedStoreListWithPageInfoResponse getSavedStoreListWithPaging(Long userId,
                                                                   SavedStoreCategoryFilterRequest filter,
                                                                   int page,
                                                                   boolean isOwner);

//    List<SavedStoreMapResponse> getStoreLocationList(Long userId,
//                                                     SavedStoreCategoryFilterRequest filter,
//                                                     boolean isOwner);

}
