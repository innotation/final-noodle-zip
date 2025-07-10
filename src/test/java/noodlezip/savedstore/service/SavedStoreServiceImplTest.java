package noodlezip.savedstore.service;

import jakarta.persistence.EntityManager;
import noodlezip.savedstore.dto.request.SaveStoreRequest;
import noodlezip.savedstore.entity.SavedStore;
import noodlezip.savedstore.entity.SavedStoreCategory;
import noodlezip.savedstore.entity.SavedStoreLocation;
import noodlezip.savedstore.repository.SavedStoreCategoryRepository;
import noodlezip.savedstore.repository.SavedStoreRepository;
import noodlezip.store.entity.Store;
import noodlezip.store.repository.StoreRepository;
import noodlezip.user.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SavedStoreServiceImplTest {

    @InjectMocks
    private SavedStoreServiceImpl savedStoreService;

    @Mock
    private EntityManager entityManager;
    @Mock
    private SavedStoreCategoryRepository saveStoreCategoryRepository;
    @Mock
    private SavedStoreRepository saveStoreRepository;
    @Mock
    private StoreRepository storeRepository;

    private User user;
    private Store store;
    private SavedStoreCategory category;

    @BeforeEach
    void setup() {
        user = User.builder().id(1L).userName("testUser").build();
        store = Store.builder().id(10L).storeName("라멘집").storeLat(37.1).storeLng(127.2).build();
        category = SavedStoreCategory.builder().id(100L).user(user).categoryName("데이트").isPublic(true).build();
    }

    @Test
    void saveSavedStore_shouldCreateNewSavedStoreIfNotExists() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("첫 방문")
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(Optional.empty());
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        SavedStoreCategory newCategory = SavedStoreCategory.builder().id(101L).user(user).categoryName("데이트").build();
        when(saveStoreCategoryRepository.save(any(SavedStoreCategory.class))).thenReturn(newCategory);

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        verify(saveStoreCategoryRepository).save(any(SavedStoreCategory.class));
        verify(saveStoreRepository).save(any(SavedStore.class));
    }

    @Test
    void saveSavedStore_shouldUpdateIfSavedStoreExists() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("재방문")
                .saveStoreCategoryIds(List.of(100L))
                .build();

        SavedStore existingSavedStore = SavedStore.builder()
                .id(999L)
                .user(user)
                .store(store)
                .saveStoreCategory(category)
                .memo("예전 메모")
                .location(new SavedStoreLocation(37.1, 127.2))
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);
        when(entityManager.getReference(SavedStoreCategory.class, 100L)).thenReturn(category);
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId()))
                .thenReturn(Optional.of(existingSavedStore));

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        assertThat(existingSavedStore.getMemo()).isEqualTo("재방문");
        assertThat(existingSavedStore.getSaveStoreCategory()).isEqualTo(category);

        verify(saveStoreRepository, never()).save(any());  // update only
    }

    @Test
    void saveSavedStore_createNewSavedStore_withNewCategory() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("처음 저장")
                .saveStoreCategoryIds(List.of(100L))
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(Optional.empty());
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        SavedStoreCategory newCategory = SavedStoreCategory.builder().id(101L).user(user).categoryName("데이트").build();
        when(saveStoreCategoryRepository.save(any(SavedStoreCategory.class))).thenReturn(newCategory);

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        verify(saveStoreCategoryRepository).save(any(SavedStoreCategory.class));
        verify(saveStoreRepository).save(any(SavedStore.class));
    }

    @Test
    void saveSavedStore_createNewSavedStore_withExistingCategoryId() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("처음 저장")
                .saveStoreCategoryIds(List.of(100L))
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);
        when(entityManager.getReference(SavedStoreCategory.class, 100L)).thenReturn(category);
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(Optional.empty());
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        verify(saveStoreRepository).save(any(SavedStore.class));
        verify(saveStoreCategoryRepository, never()).save(any());
    }

    @Test
    void saveSavedStore_updateExistingSavedStore_changeMemoAndCategory() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("수정된 메모")
                .saveStoreCategoryIds(List.of(100L))
                .build();

        SavedStore existingSavedStore = SavedStore.builder()
                .id(999L)
                .user(user)
                .store(store)
                .saveStoreCategory(category)
                .memo("이전 메모")
                .location(new SavedStoreLocation(37.1, 127.2))
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);
        when(entityManager.getReference(SavedStoreCategory.class, 100L)).thenReturn(category);
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId()))
                .thenReturn(Optional.of(existingSavedStore));

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        assertThat(existingSavedStore.getMemo()).isEqualTo("수정된 메모");
        assertThat(existingSavedStore.getSaveStoreCategory()).isEqualTo(category);
        verify(saveStoreRepository, never()).save(any());
    }

    @Test
    void saveSavedStore_createNewCategoryTrimmed() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("메모")
                .saveStoreCategoryIds(List.of(100L))
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId())).thenReturn(Optional.empty());
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        ArgumentCaptor<SavedStoreCategory> captor = ArgumentCaptor.forClass(SavedStoreCategory.class);
        verify(saveStoreCategoryRepository).save(captor.capture());
        assertThat(captor.getValue().getCategoryName()).isEqualTo("데이트");
    }

    @Test
    void deleteSavedStore_shouldCallRepository() {
        // when
        savedStoreService.deleteSavedStore(1L, 10L);

        // then
        verify(saveStoreRepository).deleteByUserIdAndStoreId(1L, 10L);
    }
}