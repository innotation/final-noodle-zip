package noodlezip.badge.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.repository.BadgeGroupRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import noodlezip.mypage.dto.BadgeGroupResponse;
import noodlezip.mypage.dto.UserBadgeResponse;
import noodlezip.mypage.dto.UserOptionBadgeResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserBadgeServiceImpl implements UserBadgeService {

    private final BadgeGroupRepository badgeGroupRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Override
    public List<BadgeGroupResponse> getBadgeGroupIds() {
        return badgeGroupRepository.getBadgeGroups();
    }

    @Override
    public List<UserBadgeResponse> findNotOptionBadgeList(Long userIde) {
        List<Long> categoryIdList = LevelBadgeCategoryType.getNotOptionBadgeCategoryIdList();

        return userBadgeRepository.findNotOptionBadgeList(userIde, categoryIdList);
    }

    @Override
    public List<UserBadgeResponse> findOptionBadgeList(Long userId) {
        List<Long> categoryIdList = LevelBadgeCategoryType.getOptionBadgeCategoryIdList();

        return userBadgeRepository.findOptionBadgeList(userId, categoryIdList);
    }

}
