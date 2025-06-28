package noodlezip.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.badge.entity.QBadge;
import noodlezip.badge.entity.QBadgeCategory;
import noodlezip.badge.entity.QUserBadge;
import noodlezip.badge.entity.UserBadge;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class UserBadgeQueryRepositoryImpl implements UserBadgeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<UserBadge> findUserLevelBadge(long userId, long badgeCategoryId) {
        QUserBadge userBadge = QUserBadge.userBadge;
        QBadge badge = QBadge.badge;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(userBadge)
                        .join(userBadge.badge, badge).fetchJoin()
                        .where(
                                userBadge.userId.eq(userId),
                                badge.badgeCategory.id.eq(badgeCategoryId)
                        )
                        .orderBy(badge.badgePolicy.badgeLevel.desc())
                        .limit(1)
                        .fetchOne()
        );
    }

    @Override
    public Optional<UserBadge> findRamenCategoryLevelUserBadge(long userId,
                                                               long badgeCategoryId,
                                                               int ramenCategoryId
    ) {
        QUserBadge userBadge = QUserBadge.userBadge;
        QBadge badge = QBadge.badge;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(userBadge)
                        .join(userBadge.badge, badge).fetchJoin()
                        .where(
                                userBadge.userId.eq(userId),
                                badge.badgeCategory.id.eq(badgeCategoryId),
                                badge.badgeExtraOption.ramenCategory.id.eq(ramenCategoryId)
                        )
                        .orderBy(badge.badgePolicy.badgeLevel.desc())
                        .limit(1)
                        .fetchOne()
        );
    }

}
