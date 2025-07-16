package noodlezip.badge.repository;

import noodlezip.badge.dto.UserNoOptionBadgeDto;
import noodlezip.badge.dto.UserOptionBadgeDto;
import noodlezip.badge.entity.UserBadge;

import java.util.List;
import java.util.Optional;

public interface UserBadgeQueryRepository {

    Optional<UserBadge> findUserLevelBadge(long userId, long badgeCategoryId);

    Optional<UserBadge> findRamenCategoryLevelUserBadge(long userId, long badgeCategoryId, int ramenCategoryId);

    Optional<UserBadge> findSidoRegionLevelUserBadge(long userId, long badgeCategoryId, int sidoRegionCode);

    List<UserBadge> findUserBadgeForMyPageProfile(Long userId);

    List<UserNoOptionBadgeDto> findNoOptionBadgeList(Long userId, List<Long> notOptionBadge);

    List<UserOptionBadgeDto> findOptionBadgeList(Long userId, List<Long> optionBadge);

}
