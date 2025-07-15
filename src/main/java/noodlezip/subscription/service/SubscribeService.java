package noodlezip.subscription.service;

import noodlezip.subscription.dto.response.SubscriptionPageResponse;

public interface SubscribeService {

    boolean isSubscribed(Long targetUserId, Long requestUserId);

    int getCountByFolloweeById(Long followeeId);

    int getCountByFollowerById(Long followerId);

    SubscriptionPageResponse getFollowerListWithPaging(Long targetUserId, Long requestUserId, int page);

    SubscriptionPageResponse getFollowingListWithPaging(Long targetUserId, Long requestUserId, int page);

    void handleSubscribe(Long targetUserId, Long requestUserId);

}
