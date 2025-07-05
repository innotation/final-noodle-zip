package noodlezip.savedstore.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.*;
import noodlezip.mypage.util.SavedStorePagePolicy;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.entity.SavedStore;
import noodlezip.savedstore.entity.SavedStoreCategory;
import noodlezip.savedstore.entity.SavedStoreLocation;
import noodlezip.savedstore.repository.SavedStoreCategoryRepository;
import noodlezip.savedstore.repository.SavedStoreRepository;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.StoreRepository;
import noodlezip.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SavedStoreServiceImpl implements SavedStoreService {

    private final EntityManager entityManager;
    private final SavedStoreCategoryRepository saveStoreCategoryRepository;
    private final SavedStoreRepository saveStoreRepository;
    private final StoreRepository storeRepository;
    private final PageUtil pageUtil;

    /// todo [가게상세-1] 가게 상세페이지 wishlist 활성화 여부 판단
    @Override
    @Transactional(readOnly = true)
    public boolean isSavedStore(Long userId, Long storeId) {
        return saveStoreRepository.existsByUserIdAndStoreId(userId, storeId);
    }

    /// todo [가게상세-2] wishlist 클릭 후 모달에 띄워질 사용자 가게저장카테고리 리스트
    @Override
    @Transactional(readOnly = true)
    public List<SavedStoreCategoryResponse> getUserSaveCategoryList(Long userId, Long storeId) {
        List<SavedStoreCategoryResponse> categoryList =
                saveStoreCategoryRepository.findUserSaveCategoryListForSearch(userId, true);
        if (categoryList.isEmpty()) {
            return categoryList;
        }

        Optional<SavedStore> savedStore = saveStoreRepository.findByUserIdAndStoreId(userId, storeId);
        if (savedStore.isPresent()) {
            SavedStore oldSavedStore = savedStore.get();
            checkCategorySavedStore(oldSavedStore, categoryList);
        } else {
            categoryList.get(0).setActive(true);
        }
        return categoryList;
    }

    private void checkCategorySavedStore(SavedStore oldSavedStore,
                                         List<SavedStoreCategoryResponse> categoryList
    ) {
        SavedStoreCategory oldCategory = oldSavedStore.getSaveStoreCategory();
        Long oldCategoryId = oldCategory.getId();

        for (SavedStoreCategoryResponse category : categoryList) {
            boolean isSameCategory = category.getCategoryId().equals(oldCategoryId);
            if (isSameCategory) {
                category.setActive(true);
            }
        }
    }


    /// todo [가게상세-3] 가게 저장 및 수정 - 모달 '저장' 버튼 클릭시
    @Override
    @Transactional
    public void saveSavedStore(Long userId, Long storeId, SaveStoreRequest saveStoreRequest) {
        Long categoryId = saveStoreRequest.getSaveStoreCategoryId();
        String newCategoryName = saveStoreRequest.getNewSavedStoreCategoryName().trim();
        String memo = saveStoreRequest.getMemo();
        User user = entityManager.getReference(User.class, userId);
        SavedStoreCategory saveStoreCategory = getSavedCategory(user, categoryId, newCategoryName);

        Optional<SavedStore> savedStore = saveStoreRepository.findByUserIdAndStoreId(userId, storeId);
        if (savedStore.isPresent()) {
            SavedStore oldSavedStore = savedStore.get();
            updateExistingSavedStore(oldSavedStore, saveStoreCategory, memo);
            return;
        }
        createNewSavedStore(user, storeId, saveStoreCategory, memo);
    }

    private void updateExistingSavedStore(SavedStore oldSavedStore, SavedStoreCategory category, String memo) {
        oldSavedStore.setSaveStoreCategory(category);
        oldSavedStore.setMemo(memo);
    }

    private void createNewSavedStore(User user,
                                     Long storeId,
                                     SavedStoreCategory category,
                                     String memo
    ) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_SAVED_STORE));

        SavedStoreLocation storeLocation = SavedStoreLocation.builder()
                .storeLat(store.getStoreLat())
                .storeLng(store.getStoreLng())
                .build();
        SavedStore saveStore = SavedStore.builder()
                .user(user)
                .store(store)
                .saveStoreCategory(category)
                .memo(memo)
                .location(storeLocation)
                .build();

        saveStoreRepository.save(saveStore);
    }

    private SavedStoreCategory getSavedCategory(User user, Long categoryId, String newCategoryName) {
        if (isNewCategory(categoryId, newCategoryName)) {
            return createNewCategory(user, newCategoryName);
        }
        return entityManager.getReference(SavedStoreCategory.class, categoryId);
    }

    private SavedStoreCategory createNewCategory(User user, String newCategoryName) {
        SavedStoreCategory newCategory = SavedStoreCategory.builder()
                .user(user)
                .categoryName(newCategoryName)
                .isPublic(true)
                .categoryOrder(0)
                .build();

        saveStoreCategoryRepository.save(newCategory);
        return newCategory;
    }

    private boolean isNewCategory(Long savedCategoryId, String newCategoryName) {
        return savedCategoryId == null && newCategoryName != null;
    }


    /// todo [가게상세-4] 가게저장 리스트에 삭제 - 모달 '삭제' 버튼 클릭시
    @Override
    @Transactional
    public void deleteSavedStore(Long userId, Long storeId) {
        saveStoreRepository.deleteByUserIdAndStoreId(userId, storeId);
    }


    @Override
    public MySavedStorePageResponse getMySavedStoreInitPage(Long userId) {
        List<SavedStoreCategoryResponse> searchFilter =
                saveStoreCategoryRepository.findUserSaveCategoryListForSearch(userId, true);
        List<SavedStoreCategoryResponse> updateCategoryList =
                saveStoreCategoryRepository.findUserSavedCategoryList(userId);
        SavedStoreListResponse savedStoreList = getSavedStoreListWithPaging(
                userId,
                SavedStoreCategoryFilterRequest.builder()
                        .categoryIdList(List.of(searchFilter.get(0).getCategoryId()))
                        .isAllCategory(false)
                        .build(),
                1, false
        );
        searchFilter.get(0).setActive(true);

        return MySavedStorePageResponse.builder()
                .searchFilter(searchFilter)
                .updateCategoryList(updateCategoryList)
                .savedStoreList(savedStoreList)
                .build();
    }

    @Override
    public SavedStorePageResponse getSavedStoreInitPage(Long userId) {
        List<SavedStoreCategoryResponse> searchFilter =
                saveStoreCategoryRepository.findUserSaveCategoryListForSearch(userId, false);
        SavedStoreListResponse savedStoreList = getSavedStoreListWithPaging(
                userId,
                SavedStoreCategoryFilterRequest.builder()
                        .categoryIdList(List.of(searchFilter.get(0).getCategoryId()))
                        .isAllCategory(false)
                        .build(),
                1, false
        );
        searchFilter.get(0).setActive(true);

        return SavedStorePageResponse.builder()
                .savedStoreList(savedStoreList)
                .savedStoreList(savedStoreList)
                .build();
    }

    @Override
    public SavedStoreListResponse getSavedStoreListWithPaging(Long userId,
                                                              SavedStoreCategoryFilterRequest filter,
                                                              int page,
                                                              boolean isOwner
    ) {
        Pageable pageable = SavedStorePagePolicy.getPageable(page);
        Page<SavedStoreResponse> storePage =
                saveStoreRepository.findSavedStoreByCategoryWithPaging(userId, filter, isOwner, pageable);

        Map<String, Object> pageInfo = pageUtil.getPageInfo(storePage, SavedStorePagePolicy.PAGE_PER_BLOCK);
        SavedStoreListResponse response = SavedStoreListResponse.of(pageInfo);
        response.setSavedStoreList(storePage.getContent());

        return response;
    }

    @Override
    public List<StoreLocationResponse> getStoreLocationList(Long userId,
                                                            SavedStoreCategoryFilterRequest filter,
                                                            boolean isOwner
    ) {
        return saveStoreRepository.getStoreLocationList(userId, filter.getCategoryIdList(), isOwner);
    }

}
