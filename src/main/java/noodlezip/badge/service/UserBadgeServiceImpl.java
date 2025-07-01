package noodlezip.badge.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.repository.BadgeGroupRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import noodlezip.mypage.dto.UserNoOptionBadgeDto;
import noodlezip.mypage.dto.UserOptionBadgeDto;
import noodlezip.mypage.dto.response.BadgeGroupResponse;
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
    public List<UserNoOptionBadgeDto> findNotOptionBadgeList(Long userIde) {
        List<Long> categoryIdList = LevelBadgeCategoryType.getNotOptionBadgeCategoryIdList();

        return userBadgeRepository.findNotOptionBadgeList(userIde, categoryIdList);
    }

    @Override
    public List<UserOptionBadgeDto> findOptionBadgeList(Long userId) {
        List<Long> categoryIdList = LevelBadgeCategoryType.getOptionBadgeCategoryIdList();

        return userBadgeRepository.findOptionBadgeList(userId, categoryIdList);
    }

}
