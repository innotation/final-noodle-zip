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
    void saveSavedStore_shouldDeleteOldAndCreateNewSavedStores() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("좋았음")
                .saveStoreCategoryIds(List.of(100L))
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);
        when(entityManager.getReference(SavedStoreCategory.class, 100L)).thenReturn(category);
        when(storeRepository.findById(store.getId())).thenReturn(Optional.of(store));

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        verify(saveStoreRepository).deleteByUserIdAndStoreId(user.getId(), store.getId());
        verify(saveStoreRepository).save(any(SavedStore.class));
    }

    @Test
    void saveSavedStore_shouldNotSaveIfCategoryIdsIsEmpty() {
        // given
        SaveStoreRequest request = SaveStoreRequest.builder()
                .memo("메모")
                .saveStoreCategoryIds(List.of())
                .build();

        when(entityManager.getReference(User.class, user.getId())).thenReturn(user);

        // when
        savedStoreService.saveSavedStore(user.getId(), store.getId(), request);

        // then
        verify(saveStoreRepository).deleteByUserIdAndStoreId(user.getId(), store.getId());
        verify(saveStoreRepository, never()).save(any());
    }

    @Test
    void getUserSavedStoreMemo_shouldReturnMemoIfExists() {
        // given
        SavedStore savedStore = SavedStore.builder()
                .memo("재방문")
                .build();
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId()))
                .thenReturn(Optional.of(savedStore));

        // when
        String memo = savedStoreService.getUserSavedStoreMemo(user.getId(), store.getId());

        // then
        assertThat(memo).isEqualTo("재방문");
    }

    @Test
    void getUserSavedStoreMemo_shouldReturnEmptyStringIfNotExists() {
        // given
        when(saveStoreRepository.findFirstByUserIdAndStoreId(user.getId(), store.getId()))
                .thenReturn(Optional.empty());

        // when
        String memo = savedStoreService.getUserSavedStoreMemo(user.getId(), store.getId());

        // then
        assertThat(memo).isEqualTo("");
    }

    @Test
    void deleteSavedStore_shouldCallRepository() {
        // when
        savedStoreService.deleteSavedStore(user.getId(), store.getId());

        // then
        verify(saveStoreRepository).deleteByUserIdAndStoreId(user.getId(), store.getId());
    }
}