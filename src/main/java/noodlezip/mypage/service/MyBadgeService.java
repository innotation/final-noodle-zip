package noodlezip.mypage.service;

import noodlezip.mypage.dto.response.badge.BadgeDetailResponse;
import noodlezip.mypage.dto.response.badge.MyBadgeBadgeResponse;

import java.util.List;

public interface MyBadgeService {

    List<MyBadgeBadgeResponse> getUserBadgeListByGroup(Long userId);

    BadgeDetailResponse getBadgeDetailList(Long userId, Long badgeId, Long badgeCategoryId);

}
