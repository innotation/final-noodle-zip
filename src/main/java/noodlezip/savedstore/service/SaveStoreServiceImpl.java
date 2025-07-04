package noodlezip.savedstore.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.entity.SaveStore;
import noodlezip.savedstore.entity.SaveStoreCategory;
import noodlezip.savedstore.entity.SaveStoreLocation;
import noodlezip.savedstore.repository.SaveStoreCategoryRepository;
import noodlezip.savedstore.repository.SaveStoreRepository;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.StoreRepository;
import noodlezip.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SaveStoreServiceImpl implements SaveStoreService {

    private final EntityManager entityManager;
    private final SaveStoreCategoryRepository saveStoreCategoryRepository;
    private final SaveStoreRepository saveStoreRepository;
    private final StoreRepository storeRepository;

    @Override
    @Transactional
    public void addSavedStore(Long userId, Long storeId, SaveStoreRequest saveStoreRequest) {
        Long categoryId = saveStoreRequest.getSaveStoreCategoryId();
        String newCategoryName = saveStoreRequest.getNewSavedStoreCategoryName().trim();
        String memo = saveStoreRequest.getMemo();

        User user = entityManager.getReference(User.class, userId);
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_SAVED_STORE));
        SaveStoreCategory saveStoreCategory = getSavedCategory(categoryId, newCategoryName);

        SaveStoreLocation storeLocation = SaveStoreLocation.builder()
                .storeLat(store.getStoreLat())
                .storeLng(store.getStoreLng())
                .build();
        SaveStore saveStore = SaveStore.builder()
                .user(user)
                .store(store)
                .saveStoreCategory(saveStoreCategory)
                .memo(memo)
                .location(storeLocation)
                .build();

        saveStoreRepository.save(saveStore);
    }

    private SaveStoreCategory getSavedCategory(Long savedCategoryId, String newCategoryName) {
        if (isNewCategory(savedCategoryId, newCategoryName)) {
            SaveStoreCategory newCategory = SaveStoreCategory.builder()
                    .categoryName(newCategoryName)
                    .isPublic(true)
                    .categoryOrder(0)
                    .build();

            saveStoreCategoryRepository.save(newCategory);
            return newCategory;
        }
        return entityManager.getReference(SaveStoreCategory.class, savedCategoryId);
    }

    private boolean isNewCategory(Long savedCategoryId, String newCategoryName) {
        return savedCategoryId == null && newCategoryName != null;
    }


    @Override
    @Transactional
    public void deleteSavedStore(Long userId, Long storeId) {
        saveStoreRepository.deleteByUserIdAndStoreId(userId, storeId);
    }


    @Override
    public List<SavedStoreCategoryResponse> getUserSaveCategoryList(Long userId, String storeId) {
        return List.of();
    }


}
