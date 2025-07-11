package noodlezip.subscription.repository;

import noodlezip.subscription.dto.response.SubscriberResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSubscriptionQueryRepository {

    Page<SubscriberResponse> findFollowerList(Long targetUserId, Long requestUserId, Pageable pageable);

    Page<SubscriberResponse> findFolloweeList(Long targetUserId, Long requestUserId, Pageable pageable);

}
