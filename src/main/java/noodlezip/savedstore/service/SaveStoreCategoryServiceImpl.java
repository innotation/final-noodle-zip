package noodlezip.savedstore.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;
import noodlezip.savedstore.entity.SaveStoreCategory;
import noodlezip.savedstore.repository.SaveStoreCategoryRepository;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SaveStoreCategoryServiceImpl implements SaveStoreCategoryService {

    private final SaveStoreCategoryRepository saveStoreCategoryRepository;

    @Override
    @Transactional
    public void updateSavedCategoryList(List<SavedStoreCategoryUpdateRequest> categoryRequests) {
        for (SavedStoreCategoryUpdateRequest categoryRequest : categoryRequests) {
            Long saveStoreCategoryId = categoryRequest.getSaveStoreCategoryId();
            String savedStoreCategoryName = categoryRequest.getSavedStoreCategoryName().trim();
            boolean isPublic = categoryRequest.isPublic();

            SaveStoreCategory saveStoreCategory = saveStoreCategoryRepository.findById(saveStoreCategoryId)
                    .orElseThrow(() -> new CustomException(SavedStoreErrorStatus._FAIL_UPDATE_SAVED_STORE_CATEGORY));

            saveStoreCategory.setCategoryName(savedStoreCategoryName);
            saveStoreCategory.setIsPublic(isPublic);
        }
    }

    @Override
    @Transactional
    public void deleteSavedCategoryList(List<Long> categoryIdList) {
        categoryIdList.forEach(saveStoreCategoryRepository::deleteById);
    }

}
