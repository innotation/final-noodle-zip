package noodlezip.subscription.repository;

import noodlezip.subscription.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>, UserSubscriptionQueryRepository {

    boolean existsByFollowerIdAndFolloweeId(long followerId, long followeeId);

    void deleteByFollowerIdAndFolloweeId(long followerId, long followeeId);

    int countByFolloweeId(Long followeeId);

    int countByFollowerId(Long followerId);

}