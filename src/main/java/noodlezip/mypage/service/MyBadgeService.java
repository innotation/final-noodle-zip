package noodlezip.mypage.service;

import noodlezip.mypage.dto.response.BadgeDetailResponse;
import noodlezip.mypage.dto.response.MyBadgeBadgeResponse;

import java.util.List;

public interface MyBadgeService {

    List<MyBadgeBadgeResponse> getUserBadgeListByGroup(Long userId);

    BadgeDetailResponse getBadgeDetailList(Long userId, Long badgeId, Long badgeCategoryId);

}
