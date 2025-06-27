package noodlezip.ramen.repository;

import noodlezip.ramen.entity.RamenCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RamenCategoryRepository extends JpaRepository<RamenCategory, Integer> {
}