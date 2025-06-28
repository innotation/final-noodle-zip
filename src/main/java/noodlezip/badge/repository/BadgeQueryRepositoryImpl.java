package noodlezip.badge.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.QBadge;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class BadgeQueryRepositoryImpl implements BadgeQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Optional<Badge> findInitLevelBadge(long badgeCategoryId) {
        QBadge badge = QBadge.badge;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(badge)
                        .where(badge.badgeCategory.id.eq(badgeCategoryId))
                        .orderBy(badge.badgePolicy.badgeLevel.asc())
                        .limit(1)
                        .fetchOne()
        );
    }

    @Override
    public Optional<Badge> findInitRamenCategoryLevelBadge(long badgeCategoryId, int ramenCategoryId) {
        QBadge badge = QBadge.badge;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(badge)
                        .where(
                                badge.badgeCategory.id.eq(badgeCategoryId),
                                badge.badgeExtraOption.ramenCategory.id.eq(ramenCategoryId)
                        )
                        .orderBy(badge.badgePolicy.badgeLevel.asc())
                        .limit(1)
                        .fetchOne()
        );
    }

}
