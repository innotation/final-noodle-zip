package noodlezip.subscription.repository;

import noodlezip.subscription.entity.UserSubscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserSubscriptionRepository extends JpaRepository<UserSubscription, Long>, UserSubscriptionQueryRepository {

    boolean existsByFollowerIdAndFolloweeId(long followerId, long followeeId);

    void deleteByFollowerIdAndFolloweeId(long followerId, long followeeId);

    // 팔로워 수(나를 팔로우하는 사람 수)
    long countByFolloweeId(Long followeeId);

    // 팔로우 수(내가 팔로우하는 사람 수)
    long countByFollowerId(Long followerId);
}