package noodlezip.badge.service;

import noodlezip.badge.dto.response.BadgeDetailResponse;
import noodlezip.badge.dto.response.MyBadgeBadgeResponse;
import noodlezip.badge.dto.response.UserBadgeResponse;

import java.util.List;

public interface MyBadgeService {

    // TODO 마이페이지 메인페이지 배지 3개
    List<UserBadgeResponse> getUserBadgeForMyPageProfile(Long userId);

    List<MyBadgeBadgeResponse> getUserBadgeListByGroup(Long userId);

    BadgeDetailResponse getBadgeDetailList(Long userId, Long badgeId, Long badgeCategoryId);

}
