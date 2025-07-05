package noodlezip.badge.service;

import noodlezip.mypage.dto.response.badge.LevelBadgeDetailResponse;

import java.util.List;

public interface BadgeService {

    List<LevelBadgeDetailResponse> getNoOptionBadgeDetails(long userId, long badgeCategoryId);

    List<LevelBadgeDetailResponse> getOptionBadgeDetails(long userId, long badgeId, long badgeCategoryId);

}
