package noodlezip.mypage.service;

import noodlezip.mypage.dto.response.MyBadgeBadgeResponse;

import java.util.List;

public interface BadgeService {

    List<MyBadgeBadgeResponse> getUserBadgeListByGroup(Long userId);

}
