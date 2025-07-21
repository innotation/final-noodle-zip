package noodlezip.subscription.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.subscription.dto.response.SubscriberResponse;
import noodlezip.subscription.entity.QUserSubscription;
import noodlezip.user.entity.ActiveStatus;
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


    @Override
    public Page<SubscriberResponse> findFollowerList(Long targetUserId,
                                                     Long requestUserId,
                                                     Pageable pageable
    ) {
        QUserSubscription subscription = QUserSubscription.userSubscription;
        QUserSubscription requestUserSubscription = new QUserSubscription("requestUserSubscription");
        QUser follower = QUser.user;

        List<SubscriberResponse> content = queryFactory
                .select(Projections.constructor(SubscriberResponse.class,
                        subscription.id,
                        follower.id,
                        follower.loginId,
                        follower.userName,
                        follower.profileImageUrl,
                        follower.profileBannerImageUrl,
                        requestUserSubscription.id.isNotNull()
                ))
                .from(subscription)
                .join(subscription.follower, follower)
                .leftJoin(requestUserSubscription)
                .on(requestUserSubscription.follower.id.eq(requestUserId)
                        .and(requestUserSubscription.followee.id.eq(follower.id)))
                .where(
                        subscription.followee.id.eq(targetUserId),
                        follower.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(subscription.id.desc())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .join(subscription.follower, follower)
                .where(
                        subscription.followee.id.eq(targetUserId),
                        follower.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }


    @Override
    public Page<SubscriberResponse> findFolloweeList(Long targetUserId,
                                                     Long requestUserId,
                                                     Pageable pageable
    ) {
        QUserSubscription subscription = QUserSubscription.userSubscription;
        QUserSubscription requestUserSubscription = new QUserSubscription("requestUserSubscription");
        QUser followee = QUser.user;

        List<SubscriberResponse> content = queryFactory
                .select(Projections.constructor(SubscriberResponse.class,
                        subscription.id,
                        followee.id,
                        followee.loginId,
                        followee.userName,
                        followee.profileImageUrl,
                        followee.profileBannerImageUrl,
                        requestUserSubscription.id.isNotNull()
                ))
                .from(subscription)
                .join(subscription.followee, followee)
                .leftJoin(requestUserSubscription)
                .on(requestUserSubscription.follower.id.eq(requestUserId)
                        .and(requestUserSubscription.followee.id.eq(followee.id)))
                .where(
                        subscription.follower.id.eq(targetUserId),
                        followee.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(subscription.id.desc())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .join(subscription.followee, followee)
                .where(
                        subscription.follower.id.eq(targetUserId),
                        followee.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }


    @Override
    public Page<SubscriberResponse> findFollowerListWithoutLoginUser(Long targetUserId, Pageable pageable) {
        QUserSubscription subscription = QUserSubscription.userSubscription;
        QUser follower = QUser.user;

        List<SubscriberResponse> content = queryFactory
                .select(Projections.constructor(SubscriberResponse.class,
                        subscription.id,
                        follower.id,
                        follower.loginId,
                        follower.userName,
                        follower.profileImageUrl,
                        follower.profileBannerImageUrl,
                        Expressions.constant(false)
                ))
                .from(subscription)
                .join(subscription.follower, follower)
                .where(
                        subscription.followee.id.eq(targetUserId),
                        follower.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(subscription.id.desc())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .join(subscription.follower, follower)
                .where(
                        subscription.followee.id.eq(targetUserId),
                        follower.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }


    @Override
    public Page<SubscriberResponse> findFolloweeListWithoutLoginUser(Long targetUserId, Pageable pageable) {
        QUserSubscription subscription = QUserSubscription.userSubscription;
        QUser followee = QUser.user;

        List<SubscriberResponse> content = queryFactory
                .select(Projections.constructor(SubscriberResponse.class,
                        subscription.id,
                        followee.id,
                        followee.loginId,
                        followee.userName,
                        followee.profileImageUrl,
                        followee.profileBannerImageUrl,
                        Expressions.constant(false)
                ))
                .from(subscription)
                .join(subscription.followee, followee)
                .where(
                        subscription.follower.id.eq(targetUserId),
                        followee.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(subscription.id.desc())
                .fetch();

        Long total = queryFactory
                .select(subscription.count())
                .from(subscription)
                .join(subscription.followee, followee)
                .where(
                        subscription.follower.id.eq(targetUserId),
                        followee.activeStatus.eq(ActiveStatus.ACTIVE)
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

}
