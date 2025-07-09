package noodlezip.badge.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.badge.dto.response.LevelBadgeDetailResponse;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.QBadge;
import noodlezip.badge.entity.QUserBadge;
import noodlezip.ramen.entity.Category;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
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

    @Override
    public Optional<Badge> findInitSidoRegionLevelBadge(long badgeCategoryId, int sidoRegionCode) {
        QBadge badge = QBadge.badge;

        return Optional.ofNullable(
                queryFactory
                        .selectFrom(badge)
                        .where(
                                badge.badgeCategory.id.eq(badgeCategoryId),
                                badge.badgeExtraOption.storeSidoLegalCode.eq(sidoRegionCode)
                        )
                        .orderBy(badge.badgePolicy.badgeLevel.asc())
                        .limit(1)
                        .fetchOne()
        );
    }


    @Override
    public List<LevelBadgeDetailResponse> findNoOptionBadgeDetails(long userId, long badgeCategoryId) {
        QBadge badge = QBadge.badge;
        QUserBadge userBadge = QUserBadge.userBadge;

        return queryFactory
                .select(Projections.constructor(LevelBadgeDetailResponse.class,
                        badge.id,
                        badge.badgeName,
                        badge.badgeImageUrl,
                        userBadge.obtainedAt
                ))
                .from(badge)
                .leftJoin(userBadge).on(
                        userBadge.badge.id.eq(badge.id)
                                .and(userBadge.userId.eq(userId))
                )
                .where(badge.badgeCategory.id.eq(badgeCategoryId))
                .fetch();
    }

    @Override
    public List<LevelBadgeDetailResponse> findOptionBadgeDetails(long userId,
                                                                 long badgeId,
                                                                 long badgeCategoryId
    ) {
        QBadge badge = QBadge.badge;
        QUserBadge userBadge = QUserBadge.userBadge;
        QBadge baseBadge = new QBadge("baseBadge");

        Badge optionBadge = queryFactory
                .selectFrom(baseBadge)
                .where(baseBadge.id.eq(badgeId))
                .fetchOne();
        if (optionBadge == null) {
            return Collections.emptyList();
        }

        Integer storeSidoLegalCodeOption = optionBadge.getBadgeExtraOption().getStoreSidoLegalCode();
        Category ramenCategoryOption = optionBadge.getBadgeExtraOption().getRamenCategory();

        BooleanBuilder where = new BooleanBuilder()
                .and(badge.badgeExtraOption.storeSidoLegalCode.coalesce(-1)
                        .eq(storeSidoLegalCodeOption != null ? storeSidoLegalCodeOption : -1))
                .and(badge.badgeExtraOption.ramenCategory.id.coalesce(-1)
                        .eq(ramenCategoryOption != null ? ramenCategoryOption.getId() : -1));

        return queryFactory
                .select(Projections.constructor(LevelBadgeDetailResponse.class,
                        badge.id,
                        badge.badgeName,
                        badge.badgeImageUrl,
                        userBadge.obtainedAt
                ))
                .from(badge)
                .leftJoin(userBadge).on(
                        userBadge.badge.id.eq(badge.id)
                                .and(userBadge.userId.eq(userId))
                )
                .where(
                        badge.badgeCategory.id.eq(badgeCategoryId),
                        where
                )
                .fetch();
    }

}
