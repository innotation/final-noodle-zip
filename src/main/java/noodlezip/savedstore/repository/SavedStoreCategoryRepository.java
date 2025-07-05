package noodlezip.savedstore.repository;

import noodlezip.savedstore.entity.SavedStoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SavedStoreCategoryRepository
        extends JpaRepository<SavedStoreCategory, Long>, SavedStoreCategoryQueryRepository {
}