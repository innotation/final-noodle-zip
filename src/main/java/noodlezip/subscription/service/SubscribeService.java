package noodlezip.subscription.service;

import noodlezip.subscription.dto.response.SubscriptionPageResponse;

public interface SubscribeService {

    // TODO 마이페이지 메인페이지 구독 버튼 활성화 여부
    boolean isSubscribed(Long targetUserId, Long requestUserId);

    SubscriptionPageResponse getFollowerListWithPaging(Long targetUserId, Long requestUserId, int page);

    SubscriptionPageResponse getFollowingListWithPaging(Long targetUserId, Long requestUserId, int page);

    void handleSubscribe(Long targetUserId, Long requestUserId);

}
