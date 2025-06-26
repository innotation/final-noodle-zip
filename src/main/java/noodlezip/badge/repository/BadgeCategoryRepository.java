package noodlezip.badge.repository;

import noodlezip.badge.entity.BadgeCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeCategoryRepository extends JpaRepository<BadgeCategory, Long> {
}