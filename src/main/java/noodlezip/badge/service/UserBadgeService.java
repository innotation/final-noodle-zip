package noodlezip.badge.service;

import noodlezip.mypage.dto.BadgeGroupResponse;
import noodlezip.mypage.dto.UserBadgeResponse;
import noodlezip.mypage.dto.UserOptionBadgeResponse;

import java.util.List;

public interface UserBadgeService {

    List<BadgeGroupResponse> getBadgeGroupIds();

    List<UserBadgeResponse> findNotOptionBadgeList(Long userId);

    List<UserBadgeResponse> findOptionBadgeList(Long userId);

}
