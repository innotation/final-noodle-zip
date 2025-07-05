package noodlezip.savedstore.repository;

import noodlezip.savedstore.entity.SavedStoreCategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SavedStoreCategoryRepository
        extends JpaRepository<SavedStoreCategory, Long>, SavedStoreCategoryQueryRepository {

    @EntityGraph(attributePaths = "saveStoreList")
    Optional<SavedStoreCategory> findWithSaveStoresById(Long categoryId);

}