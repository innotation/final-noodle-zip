package noodlezip.savedstore.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.savedstore.dto.request.AddCategoryRequest;
import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.util.SavedStorePagePolicy;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.dto.response.SavedStoreListWithPageInfoResponse;
import noodlezip.savedstore.dto.response.SavedStoreResponse;
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

    /// todo \[가게상세-1] 가게 상세페이지 wishlist 활성화 여부 판단
    @Override
    @Transactional(readOnly = true)
    public boolean isSavedStore(Long userId, Long storeId) {
        return saveStoreRepository.existsByUserIdAndStoreId(userId, storeId);
    }

    /// todo \[가게상세-2] wishlist 클릭 후 모달에 띄워질 사용자 가게저장카테고리 리스트
    @Override
    @Transactional(readOnly = true)
    public List<SavedStoreCategoryResponse> getUserSaveCategoryList(Long userId, Long storeId) {
        List<SavedStoreCategoryResponse> categoryList =
                saveStoreCategoryRepository.findUserSaveCategoryListForSearch(userId, true);
        if (categoryList.isEmpty()) {
            return categoryList;
        }

        // 여러 저장된 매장들을 가져와서 각각의 카테고리를 체크
        List<SavedStore> savedStores = saveStoreRepository.findAllByUserIdAndStoreId(userId, storeId);
        if (!savedStores.isEmpty()) {
            checkCategorySavedStoreMultiple(savedStores, categoryList);
        } else {
            categoryList.get(0).setActive(true);
        }
        return categoryList;
    }

    private void checkCategorySavedStoreMultiple(List<SavedStore> savedStores,
                                         List<SavedStoreCategoryResponse> categoryList
    ) {
        for (SavedStore savedStore : savedStores) {
            SavedStoreCategory savedCategory = savedStore.getSaveStoreCategory();
            Long savedCategoryId = savedCategory.getId();

            for (SavedStoreCategoryResponse category : categoryList) {
                boolean isSameCategory = category.getCategoryId().equals(savedCategoryId);
                if (isSameCategory) {
                    category.setActive(true);
                }
            }
        }
    }


    /// todo \[가게상세-3] 가게 저장 및 수정 - 모달 '저장' 버튼 클릭시
    @Override
    @Transactional
    public void saveSavedStore(Long userId, Long storeId, SaveStoreRequest saveStoreRequest) {
        List<Long> categoryIds = saveStoreRequest.getSaveStoreCategoryIds();
        List<String> newCategoryNames = saveStoreRequest.getNewSavedStoreCategoryNames();
        String memo = saveStoreRequest.getMemo();
        User user = entityManager.getReference(User.class, userId);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_SAVED_STORE));

        // 기존 저장된 매장들 삭제
        saveStoreRepository.deleteByUserIdAndStoreId(userId, storeId);

        // 기존 카테고리들에 저장
        if (categoryIds != null && !categoryIds.isEmpty()) {
            for (Long categoryId : categoryIds) {
                SavedStoreCategory category = entityManager.getReference(SavedStoreCategory.class, categoryId);
                createNewSavedStore(user, storeId, category, memo);
            }
        }

        // 새 카테고리들 생성 후 저장
        if (newCategoryNames != null && !newCategoryNames.isEmpty()) {
            for (String newCategoryName : newCategoryNames) {
                if (newCategoryName != null && !newCategoryName.trim().isEmpty()) {
                    SavedStoreCategory newCategory = createNewCategory(user, newCategoryName.trim());
                    createNewSavedStore(user, storeId, newCategory, memo);
                }
            }
        }
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
            return createNewCategory(user, newCategoryName.trim());
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


    /// todo \[가게상세-4] 가게저장 리스트에 삭제 - 모달 '삭제' 버튼 클릭시
    @Override
    @Transactional
    public void deleteSavedStore(Long userId, Long storeId) {
        saveStoreRepository.deleteByUserIdAndStoreId(userId, storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedStoreCategoryResponse> getSaveCategoryListForSearch(Long userId, boolean isOwner) {
        return saveStoreCategoryRepository.findUserSaveCategoryListForSearch(userId, isOwner);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedStoreCategoryResponse> getSavedCategoryList(Long userId) {
        return saveStoreCategoryRepository.findUserSavedCategoryList(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public SavedStoreListWithPageInfoResponse getSavedStoreListWithPaging(Long userId,
                                                                          SavedStoreCategoryFilterRequest filter,
                                                                          int page,
                                                                          boolean isOwner
    ) {
        Pageable pageable = SavedStorePagePolicy.getPageable(page);
        Page<SavedStoreResponse> storePage =
                saveStoreRepository.findSavedStoreByCategoryWithPaging(userId, filter, isOwner, pageable);

        Map<String, Object> pageInfo = pageUtil.getPageInfo(storePage, SavedStorePagePolicy.PAGE_PER_BLOCK);
        SavedStoreListWithPageInfoResponse response = SavedStoreListWithPageInfoResponse.of(pageInfo);
        response.setSavedStoreList(storePage.getContent());

        return response;
    }

    @Override
    @Transactional
    public void addSavedStoreCategory(Long userId, AddCategoryRequest addCategoryRequest) {
        User user = entityManager.getReference(User.class, userId);
        SavedStoreCategory newCategory = SavedStoreCategory.builder()
                .user(user)
                .categoryName(addCategoryRequest.getCategoryName())
                .isPublic(addCategoryRequest.isPublic())
                .categoryOrder(0)
                .build();
        saveStoreCategoryRepository.save(newCategory);
    }

}
