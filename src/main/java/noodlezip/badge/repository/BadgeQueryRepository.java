package noodlezip.badge.repository;

import noodlezip.badge.entity.Badge;

import java.util.Optional;

public interface BadgeQueryRepository {

    Optional<Badge> findInitLevelBadge(long badgeCategoryId);

    Optional<Badge> findInitRamenCategoryLevelBadge(long badgeCategoryId, int ramenCategoryId);

}
