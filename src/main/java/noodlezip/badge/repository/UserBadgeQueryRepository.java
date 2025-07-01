package noodlezip.badge.repository;

import noodlezip.badge.entity.UserBadge;
import noodlezip.mypage.dto.UserNoOptionBadgeDto;
import noodlezip.mypage.dto.UserOptionBadgeDto;

import java.util.List;
import java.util.Optional;

public interface UserBadgeQueryRepository {

    Optional<UserBadge> findUserLevelBadge(long userId, long badgeCategoryId);

    Optional<UserBadge> findRamenCategoryLevelUserBadge(long userId,
                                                        long badgeCategoryId,
                                                        int ramenCategoryId);

    Optional<UserBadge> findSidoRegionLevelUserBadge(long userId,
                                                     long badgeCategoryId,
                                                     int sidoRegionCode);

    List<UserNoOptionBadgeDto> findNotOptionBadgeList(Long userId, List<Long> notOptionBadge);

    List<UserOptionBadgeDto> findOptionBadgeList(Long userId, List<Long> optionBadge);

}
