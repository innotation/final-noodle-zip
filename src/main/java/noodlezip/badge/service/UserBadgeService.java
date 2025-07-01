package noodlezip.badge.service;

import noodlezip.mypage.dto.UserNoOptionBadgeDto;
import noodlezip.mypage.dto.UserOptionBadgeDto;
import noodlezip.mypage.dto.response.BadgeGroupResponse;

import java.util.List;

public interface UserBadgeService {

    List<BadgeGroupResponse> getBadgeGroupIds();

    List<UserNoOptionBadgeDto> findNotOptionBadgeList(Long userId);

    List<UserOptionBadgeDto> findOptionBadgeList(Long userId);

}
