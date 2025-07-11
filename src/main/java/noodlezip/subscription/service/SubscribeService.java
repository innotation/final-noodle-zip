package noodlezip.subscription.service;

import noodlezip.subscription.dto.response.SubscriptionPageResponse;

public interface SubscribeService {

    SubscriptionPageResponse getFollowerListWithPaging(Long targetUserId, Long requestUserId, int page);

    SubscriptionPageResponse getFollowingListWithPaging(Long targetUserId, Long requestUserId, int page);

    void handleSubscribe(Long targetUserId, Long requestUserId);

}
