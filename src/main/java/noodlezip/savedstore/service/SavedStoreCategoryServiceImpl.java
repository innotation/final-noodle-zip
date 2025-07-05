package noodlezip.savedstore.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;
import noodlezip.savedstore.entity.SavedStoreCategory;
import noodlezip.savedstore.repository.SavedStoreCategoryRepository;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SavedStoreCategoryServiceImpl implements SavedStoreCategoryService {

    private final SavedStoreCategoryRepository saveStoreCategoryRepository;

    @Override
    @Transactional
    public void updateSavedCategoryList(Long userId, List<SavedStoreCategoryUpdateRequest> categoryRequests) {
        for (SavedStoreCategoryUpdateRequest categoryRequest : categoryRequests) {
            Long saveStoreCategoryId = categoryRequest.getSaveStoreCategoryId();
            String savedStoreCategoryName = categoryRequest.getSavedStoreCategoryName().trim();
            boolean isPublic = categoryRequest.isPublic();

            SavedStoreCategory category = saveStoreCategoryRepository.findById(saveStoreCategoryId)
                    .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_UPDATE_SAVED_STORE_CATEGORY));

            if (!category.validateOwner(userId)) {
                throw new CustomException(SavedStoreErrorStatus._UNAUTHORIZED_SAVED_STORE_ACCESS);
            }

            category.setCategoryName(savedStoreCategoryName);
            category.setIsPublic(isPublic);
        }
    }

    @Override
    @Transactional
    public void deleteSavedCategoryList(Long userId, List<Long> categoryIdList) {
        for (Long categoryId : categoryIdList) {
            SavedStoreCategory category = saveStoreCategoryRepository.findById(categoryId)
                    .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_DELETE_SAVED_STORE_CATEGORY));

            if (!category.validateOwner(userId)) {
                throw new CustomException(SavedStoreErrorStatus._UNAUTHORIZED_SAVED_STORE_ACCESS);
            }

            saveStoreCategoryRepository.delete(category);
        }
    }

}
