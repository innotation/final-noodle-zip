package noodlezip.mypage.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString

public class MyBadgeBadgeResponse {

    private BadgeGroupResponse badgeGroup;
    private List<UserBadgeResponse> userBadgeList;

}
