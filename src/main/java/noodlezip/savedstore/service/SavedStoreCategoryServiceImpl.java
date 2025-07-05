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
    public void updateSavedCategoryList(List<SavedStoreCategoryUpdateRequest> categoryRequests) {
        for (SavedStoreCategoryUpdateRequest categoryRequest : categoryRequests) {
            Long saveStoreCategoryId = categoryRequest.getSaveStoreCategoryId();
            String savedStoreCategoryName = categoryRequest.getSavedStoreCategoryName().trim();
            boolean isPublic = categoryRequest.isPublic();

            SavedStoreCategory savedStoreCategory = saveStoreCategoryRepository.findById(saveStoreCategoryId)
                    .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_UPDATE_SAVED_STORE_CATEGORY));

            savedStoreCategory.setCategoryName(savedStoreCategoryName);
            savedStoreCategory.setIsPublic(isPublic);
        }
    }

    @Override
    @Transactional
    public void deleteSavedCategoryList(List<Long> categoryIdList) {
        categoryIdList.forEach(saveStoreCategoryRepository::deleteById);
    }

}
