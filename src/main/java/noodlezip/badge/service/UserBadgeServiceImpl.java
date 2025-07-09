package noodlezip.badge.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.dto.UserNoOptionBadgeDto;
import noodlezip.badge.dto.UserOptionBadgeDto;
import noodlezip.badge.dto.response.BadgeGroupResponse;
import noodlezip.badge.repository.BadgeGroupRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class UserBadgeServiceImpl implements UserBadgeService {

    private final BadgeGroupRepository badgeGroupRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Override
    @Transactional(readOnly = true)
    public List<BadgeGroupResponse> getBadgeGroupList() {
        return badgeGroupRepository.getBadgeGroups();
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserNoOptionBadgeDto> getNoOptionUserBadgeList(Long userIde) {
        List<Long> categoryIdList = LevelBadgeCategoryType.getNoOptionBadgeCategoryIdList();

        return userBadgeRepository.findNoOptionBadgeList(userIde, categoryIdList);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserOptionBadgeDto> getOptionUserBadgeList(Long userId) {
        List<Long> categoryIdList = LevelBadgeCategoryType.getOptionBadgeCategoryIdList();

        return userBadgeRepository.findOptionBadgeList(userId, categoryIdList);
    }

}
