package noodlezip.savedstore.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.savedstore.dto.request.AddCategoryRequest;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.request.SavedStoreCategoryFilterRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.dto.response.SavedStoreListWithPageInfoResponse;
import noodlezip.savedstore.dto.response.SavedStoreResponse;
import noodlezip.savedstore.entity.SavedStore;
import noodlezip.savedstore.entity.SavedStoreCategory;
import noodlezip.savedstore.entity.SavedStoreLocation;
import noodlezip.savedstore.repository.SavedStoreCategoryRepository;
import noodlezip.savedstore.repository.SavedStoreRepository;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
import noodlezip.savedstore.util.SavedStorePagePolicy;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.StoreRepository;
import noodlezip.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SavedStoreServiceImpl implements SavedStoreService {

    private final EntityManager entityManager;
    private final SavedStoreCategoryRepository saveStoreCategoryRepository;
    private final SavedStoreRepository saveStoreRepository;
    private final StoreRepository storeRepository;
    private final PageUtil pageUtil;

    @Override
    @Transactional(readOnly = true)
    public boolean isSavedStore(Long userId, Long storeId) {
        return saveStoreRepository.existsByUserIdAndStoreId(userId, storeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SavedStoreCategoryResponse> getUserSaveCategoryList(Long userId, Long storeId) {
        List<SavedStoreCategoryResponse> categoryList =
                saveStoreCategoryRepository.findUserSaveCategoryListForSearch(userId, true);
        if (categoryList.isEmpty()) {
            return categoryList;
        }

        List<SavedStore> savedStores = saveStoreRepository.findAllByUserIdAndStoreId(userId, storeId);
        if (!savedStores.isEmpty()) {
            checkCategorySavedStoreMultiple(savedStores, categoryList);
        }
        return categoryList;
    }

    @Override
    public String getUserSavedStoreMemo(Long userId, Long storeId) {
        return saveStoreRepository.findFirstByUserIdAndStoreId(userId, storeId)
                .map(SavedStore::getMemo)
                .orElse("");
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

    @Override
    @Transactional
    public void saveSavedStore(Long userId, Long storeId, SaveStoreRequest saveStoreRequest) {
        List<Long> categoryIds = saveStoreRequest.getSaveStoreCategoryIds();
        String memo = saveStoreRequest.getMemo();
        User user = entityManager.getReference(User.class, userId);

        saveStoreRepository.deleteByUserIdAndStoreId(userId, storeId);

        if (categoryIds == null || categoryIds.isEmpty()) {
            return;
        }
        for (Long categoryId : categoryIds) {
            createNewSavedStore(user, storeId, categoryId, memo);
        }
    }

    private void createNewSavedStore(User user,
                                     Long storeId,
                                     Long categoryId,
                                     String memo
    ) {
        SavedStoreCategory category = entityManager.getReference(SavedStoreCategory.class, categoryId);
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

}
