package noodlezip.mypage.service;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.service.UserBadgeService;
import noodlezip.mypage.dto.BadgeGroupResponse;
import noodlezip.mypage.dto.UserBadgeResponse;
import noodlezip.mypage.dto.UserOptionBadgeResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class MyBadgeServiceImpl implements MyBadgeService {

    private final UserBadgeService userBadgeService;

    public Map<BadgeGroupResponse, List<Object>> getUserBadgeListByGroup(Long userId) {



        List<BadgeGroupResponse> badgeGroupList = userBadgeService.getBadgeGroupIds();
        List<UserBadgeResponse> notOptionBadgeList = userBadgeService.findNotOptionBadgeList(userId);
        List<UserBadgeResponse> optionBadgeList = userBadgeService.findOptionBadgeList(userId);

        /**
         * 그룹별로 ㅎ나로 합침
         * 프론트에서 띄워질 결과 어차피 배니타이틀이름, 레벨타이블로 나눠ㅜ저장함
         */

        return null;
    }
}
