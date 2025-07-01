package noodlezip.mypage.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.service.UserBadgeService;
import noodlezip.mypage.dto.UserNoOptionBadgeDto;
import noodlezip.mypage.dto.UserOptionBadgeDto;
import noodlezip.mypage.dto.response.BadgeGroupResponse;
import noodlezip.mypage.dto.response.MyBadgeBadgeResponse;
import noodlezip.mypage.dto.response.UserBadgeResponse;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class BadgeServiceImpl implements BadgeService {

    private final UserBadgeService userBadgeService;

    /// 존재하는 사용자인지 한상 판단하고 하기

    @Override
    public List<MyBadgeBadgeResponse> getUserBadgeListByGroup(Long userId) {
        List<BadgeGroupResponse> badgeGroupList = userBadgeService.getBadgeGroupIds();
        List<UserNoOptionBadgeDto> notOptionBadgeList = userBadgeService.findNotOptionBadgeList(userId);
        List<UserOptionBadgeDto> optionBadgeList = userBadgeService.findOptionBadgeList(userId);

        return mergeBadgeListResultByGroup(badgeGroupList, notOptionBadgeList, optionBadgeList);
    }

    private List<MyBadgeBadgeResponse> mergeBadgeListResultByGroup(List<BadgeGroupResponse> badgeGroupList,
                                                                   List<UserNoOptionBadgeDto> notOptionBadgeList,
                                                                   List<UserOptionBadgeDto> optionBadgeList

    ) {
        List<MyBadgeBadgeResponse> result = new ArrayList<>();

        for (BadgeGroupResponse badgeGroupResponse : badgeGroupList) {
            Long groupId = badgeGroupResponse.getId();

            List<UserBadgeResponse> mergedList = new ArrayList<>();

            for (UserNoOptionBadgeDto dto : notOptionBadgeList) {
                if (dto.getBadgeGroupId().equals(groupId)) {
                    mergedList.add(dto.toUserBadgeResponse());
                }
            }
            for (UserOptionBadgeDto dto : optionBadgeList) {
                if (dto.getBadgeGroupId().equals(groupId)) {
                    mergedList.add(dto.toUserBadgeResponse());
                }
            }

            result.add(
                    MyBadgeBadgeResponse.builder()
                            .badgeGroup(badgeGroupResponse)
                            .userBadgeList(mergedList)
                            .build()
            );
        }
        return result;
    }

}
