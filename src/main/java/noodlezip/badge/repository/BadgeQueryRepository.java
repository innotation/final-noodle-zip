package noodlezip.badge.repository;

import noodlezip.badge.entity.Badge;
import noodlezip.mypage.dto.response.LevelBadgeDetailResponse;

import java.util.List;
import java.util.Optional;

public interface BadgeQueryRepository {

    Optional<Badge> findInitLevelBadge(long badgeCategoryId);

    Optional<Badge> findInitRamenCategoryLevelBadge(long badgeCategoryId, int ramenCategoryId);

    Optional<Badge> findInitSidoRegionLevelBadge(long badgeCategoryId, int sidoRegionCode);

    List<LevelBadgeDetailResponse> findNoOptionBadgeDetails(long userId, long badgeCategoryId);

    List<LevelBadgeDetailResponse> findOptionBadgeDetails(long userId,
                                                          long badgeId,
                                                          long badgeCategoryId);

}
