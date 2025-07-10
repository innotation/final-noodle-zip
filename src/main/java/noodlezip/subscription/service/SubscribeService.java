package noodlezip.subscription.service;

import noodlezip.subscription.dto.response.FollowerPageResponse;
import noodlezip.subscription.dto.response.FollowingPageResponse;

public interface SubscribeService {

    FollowerPageResponse getFollowerListWithPaging(Long targetUserId, Long requestUserId, int page);

    FollowingPageResponse getFollowingListWithPaging(Long targetUserId, Long requestUserId, int page);

    void handleSubscribe(Long targetUserId, Long requestUserId);

}
