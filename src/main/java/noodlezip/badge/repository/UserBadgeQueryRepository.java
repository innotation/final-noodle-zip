package noodlezip.badge.repository;

import noodlezip.badge.entity.UserBadge;
import noodlezip.mypage.dto.UserBadgeResponse;
import noodlezip.mypage.dto.UserOptionBadgeResponse;

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

    // 나의배지 다른사람배지 모듈화해서
    List<UserBadgeResponse> findNotOptionBadgeList(Long userId, List<Long> notOptionBadge);

    List<UserBadgeResponse> findOptionBadgeList(Long userId, List<Long> optionBadge);

}
