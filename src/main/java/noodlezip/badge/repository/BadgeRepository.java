package noodlezip.badge.repository;

import noodlezip.badge.entity.Badge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BadgeRepository extends JpaRepository<Badge, Long>, BadgeQueryRepository {
}
