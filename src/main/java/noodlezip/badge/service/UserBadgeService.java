package noodlezip.badge.service;

import noodlezip.badge.dto.UserNoOptionBadgeDto;
import noodlezip.badge.dto.UserOptionBadgeDto;
import noodlezip.mypage.dto.response.badge.BadgeGroupResponse;

import java.util.List;

public interface UserBadgeService {

    List<BadgeGroupResponse> getBadgeGroupIds();

    List<UserNoOptionBadgeDto> getNoOptionUserBadgeList(Long userId);

    List<UserOptionBadgeDto> getOptionUserBadgeList(Long userId);

}
