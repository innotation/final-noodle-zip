package noodlezip.savedstore.service;

import noodlezip.common.exception.CustomException;
import noodlezip.savedstore.dto.request.SavedStoreCategoryUpdateRequest;
import noodlezip.savedstore.entity.SavedStoreCategory;
import noodlezip.savedstore.repository.SavedStoreCategoryRepository;
import noodlezip.savedstore.status.SavedStoreErrorStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavedStoreCategoryServiceImplTest {

    @InjectMocks
    private SavedStoreCategoryServiceImpl savedStoreCategoryService;

    @Mock
    private SavedStoreCategoryRepository saveStoreCategoryRepository;

    @Test
    void updateSavedCategoryList_shouldUpdateAllValidCategories() {
        // given
        Long userId = 1L;
        SavedStoreCategoryUpdateRequest req1 = new SavedStoreCategoryUpdateRequest();
        req1.setSaveStoreCategoryId(100L);
        req1.setSavedStoreCategoryName("데이트");
        req1.setIsPublic(true);

        SavedStoreCategory category = mock(SavedStoreCategory.class);
        when(saveStoreCategoryRepository.findById(100L)).thenReturn(Optional.of(category));
        when(category.validateOwner(userId)).thenReturn(true);

        // when
        savedStoreCategoryService.updateSavedCategoryList(userId, List.of(req1));

        // then
        verify(category).setCategoryName("데이트");
        verify(category).setIsPublic(true);
    }

    @Test
    void updateSavedCategoryList_shouldThrow_whenCategoryNotFound() {
        // given
        Long userId = 1L;
        SavedStoreCategoryUpdateRequest req = new SavedStoreCategoryUpdateRequest();
        req.setSaveStoreCategoryId(999L);
        req.setSavedStoreCategoryName("라멘");
        req.setIsPublic(false);

        when(saveStoreCategoryRepository.findById(999L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() ->
                savedStoreCategoryService.updateSavedCategoryList(userId, List.of(req))
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(SavedStoreErrorStatus._FAIL_UPDATE_SAVED_STORE_CATEGORY.getMessage());
    }

    @Test
    void updateSavedCategoryList_shouldThrow_whenUnauthorizedOwner() {
        // given
        Long userId = 1L;
        SavedStoreCategoryUpdateRequest req = new SavedStoreCategoryUpdateRequest();
        req.setSaveStoreCategoryId(100L);
        req.setSavedStoreCategoryName("라멘");
        req.setIsPublic(true);

        SavedStoreCategory category = mock(SavedStoreCategory.class);
        when(saveStoreCategoryRepository.findById(100L)).thenReturn(Optional.of(category));
        when(category.validateOwner(userId)).thenReturn(false);

        // then
        assertThatThrownBy(() ->
                savedStoreCategoryService.updateSavedCategoryList(userId, List.of(req))
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(SavedStoreErrorStatus._UNAUTHORIZED_SAVED_STORE_ACCESS.getMessage());
    }

    @Test
    void deleteSavedCategoryList_shouldDeleteAllValidCategories() {
        // given
        Long userId = 1L;
        Long categoryId = 200L;

        SavedStoreCategory category = mock(SavedStoreCategory.class);
        when(saveStoreCategoryRepository.findWithSaveStoresById(categoryId)).thenReturn(Optional.of(category));
        when(category.validateOwner(userId)).thenReturn(true);

        // when
        savedStoreCategoryService.deleteSavedCategoryList(userId, List.of(categoryId));

        // then
        verify(saveStoreCategoryRepository).delete(category);
    }

    @Test
    void deleteSavedCategoryList_shouldThrow_whenNotFound() {
        // given
        Long userId = 1L;
        when(saveStoreCategoryRepository.findWithSaveStoresById(999L)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() ->
                savedStoreCategoryService.deleteSavedCategoryList(userId, List.of(999L))
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(SavedStoreErrorStatus._FAIL_DELETE_SAVED_STORE_CATEGORY.getMessage());
    }

    @Test
    void deleteSavedCategoryList_shouldThrow_whenUnauthorized() {
        // given
        Long userId = 1L;
        SavedStoreCategory category = mock(SavedStoreCategory.class);
        when(saveStoreCategoryRepository.findWithSaveStoresById(300L)).thenReturn(Optional.of(category));
        when(category.validateOwner(userId)).thenReturn(false);

        // then
        assertThatThrownBy(() ->
                savedStoreCategoryService.deleteSavedCategoryList(userId, List.of(300L))
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(SavedStoreErrorStatus._UNAUTHORIZED_SAVED_STORE_ACCESS.getMessage());
    }
}