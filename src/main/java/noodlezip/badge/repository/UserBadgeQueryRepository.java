package noodlezip.badge.repository;

import noodlezip.badge.entity.UserBadge;

import java.util.Optional;

public interface UserBadgeQueryRepository {

    Optional<UserBadge> findUserLevelBadge(long userId, long badgeCategoryId);

    Optional<UserBadge> findRamenCategoryLevelUserBadge(long userId,
                                                        long badgeCategoryId,
                                                        int ramenCategoryId);

    Optional<UserBadge> findSidoRegionLevelUserBadge(long userId,
                                                     long badgeCategoryId,
                                                     int sidoRegionCode);

}
