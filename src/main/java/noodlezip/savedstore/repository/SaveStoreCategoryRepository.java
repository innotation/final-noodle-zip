package noodlezip.savedstore.repository;

import noodlezip.savedstore.entity.SaveStoreCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SaveStoreCategoryRepository extends JpaRepository<SaveStoreCategory, Long> {
}