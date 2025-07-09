package noodlezip.badge.service;

import noodlezip.badge.dto.response.BadgeDetailResponse;
import noodlezip.badge.dto.response.MyBadgeBadgeResponse;

import java.util.List;

public interface MyBadgeService {

    List<MyBadgeBadgeResponse> getUserBadgeListByGroup(Long userId);

    BadgeDetailResponse getBadgeDetailList(Long userId, Long badgeId, Long badgeCategoryId);

}
