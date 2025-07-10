package noodlezip.badge.service;

import noodlezip.badge.dto.UserNoOptionBadgeDto;
import noodlezip.badge.dto.UserOptionBadgeDto;
import noodlezip.badge.dto.response.BadgeGroupResponse;

import java.util.List;

public interface UserBadgeService {

    List<BadgeGroupResponse> getBadgeGroupList();

    List<UserNoOptionBadgeDto> getNoOptionUserBadgeList(Long userId);

    List<UserOptionBadgeDto> getOptionUserBadgeList(Long userId);

}
