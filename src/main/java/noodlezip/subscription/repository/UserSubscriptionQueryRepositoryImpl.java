package noodlezip.subscription.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.subscription.dto.response.FolloweeResponse;
import noodlezip.subscription.dto.response.FollowerResponse;
import noodlezip.subscription.entity.QUserSubscription;
import noodlezip.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class UserSubscriptionQueryRepositoryImpl implements UserSubscriptionQueryRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * targetUserId가 following인 데이터의 follwer
     */
//    public List<FollowerResponse> findFollowerList(Long targetUserId, Long requestUserId) {
//        QUserSubscription subscription = QUserSubscription.userSubscription;
//        QUserSubscription requestUserSubscription = new QUserSubscription("requestUserSubscription");    // requestUser 의 구독 정보
//        QUser follower = QUser.user;
//
//        return queryFactory
//                .select(Projections.constructor(FollowerResponse.class,
//                        subscription.id,
//                        follower.id,
//                        follower.userName,
//                        follower.profileImageUrl,
//                        requestUserSubscription.id.isNotNull()
//                ))
//                .from(subscription)
//                .join(subscription.follower, follower)
//                .leftJoin(requestUserSubscription)
//                .on(requestUserSubscription.follower.id.eq(requestUserId)
//                        .and(requestUserSubscription.followee.id.eq(follower.id)))
//                .where(subscription.followee.id.eq(targetUserId))
//                .fetch();
//    }
    public Page<FollowerResponse> findFollowerList(Long targetUserId,
                                                   Long requestUserId,
                                                   Pageable pageable
    ) {
        QUserSubscription subscription = QUserSubscription.userSubscription;
        QUserSubscription requestUserSubscription = new QUserSubscription("requestUserSubscription");    // requestUser 의 구독 정보
        QUser follower = QUser.user;

        List<FollowerResponse> content = queryFactory
                .select(Projections.constructor(FollowerResponse.class,
                        subscription.id,
                        follower.id,
                        follower.userName,
                        follower.profileImageUrl,
                        requestUserSubscription.id.isNotNull()
                ))
                .from(subscription)
                .join(subscription.follower, follower)
                .leftJoin(requestUserSubscription)
                .on(requestUserSubscription.follower.id.eq(requestUserId)
                        .and(requestUserSubscription.followee.id.eq(follower.id)))
                .where(subscription.followee.id.eq(targetUserId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(subscription.id.desc())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .where(subscription.followee.id.eq(targetUserId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    /**
     * targetUserId가 follower
     */
//    public List<FolloweeResponse> findFolloweeList(Long targetUserId, Long requestUserId) {
//        QUserSubscription subscription = QUserSubscription.userSubscription;
//        QUserSubscription requestUserSubscription = new QUserSubscription("requestUserSubscription");
//        QUser followee = QUser.user;
//
//        return queryFactory
//                .select(Projections.constructor(FolloweeResponse.class,
//                        subscription.id,
//                        followee.id,
//                        followee.userName,
//                        followee.profileImageUrl,
//                        requestUserSubscription.id.isNotNull()
//                ))
//                .from(subscription)
//                .join(subscription.followee, followee)
//                .leftJoin(requestUserSubscription)
//                .on(requestUserSubscription.follower.id.eq(requestUserId)
//                        .and(requestUserSubscription.followee.id.eq(followee.id)))
//                .where(subscription.follower.id.eq(targetUserId))
//                .fetch();
//    }
    public Page<FolloweeResponse> findFolloweeList(Long targetUserId,
                                                   Long requestUserId,
                                                   Pageable pageable
    ) {
        QUserSubscription subscription = QUserSubscription.userSubscription;
        QUserSubscription requestUserSubscription = new QUserSubscription("requestUserSubscription");
        QUser followee = QUser.user;

        List<FolloweeResponse> content = queryFactory
                .select(Projections.constructor(FolloweeResponse.class,
                        subscription.id,
                        followee.id,
                        followee.userName,
                        followee.profileImageUrl,
                        requestUserSubscription.id.isNotNull()
                ))
                .from(subscription)
                .join(subscription.followee, followee)
                .leftJoin(requestUserSubscription)
                .on(requestUserSubscription.follower.id.eq(requestUserId)
                        .and(requestUserSubscription.followee.id.eq(followee.id)))
                .where(subscription.follower.id.eq(targetUserId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(subscription.id.desc())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .where(subscription.follower.id.eq(targetUserId))
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

}
