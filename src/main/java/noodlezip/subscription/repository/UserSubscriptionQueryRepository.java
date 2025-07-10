package noodlezip.subscription.repository;

import noodlezip.subscription.dto.response.FolloweeResponse;
import noodlezip.subscription.dto.response.FollowerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserSubscriptionQueryRepository {

    Page<FollowerResponse> findFollowerList(Long targetUserId, Long requestUserId, Pageable pageable);

    Page<FolloweeResponse> findFolloweeList(Long targetUserId, Long requestUserId, Pageable pageable);

}
