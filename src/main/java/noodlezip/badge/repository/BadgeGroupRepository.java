package noodlezip.badge.repository;

import noodlezip.badge.entity.BadgeGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeGroupRepository extends JpaRepository<BadgeGroup, Long>, BadgeGroupQueryRepository {
}