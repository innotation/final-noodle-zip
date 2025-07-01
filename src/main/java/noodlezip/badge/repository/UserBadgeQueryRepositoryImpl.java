package noodlezip.badge.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.badge.entity.*;
import noodlezip.mypage.dto.UserBadgeResponse;
import noodlezip.mypage.dto.UserOptionBadgeResponse;
import noodlezip.ramen.entity.QCategory;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.List;
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

    @Override
    public Optional<UserBadge> findSidoRegionLevelUserBadge(long userId,
                                                            long badgeCategoryId,
                                                            int sidoRegionCode
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
                                badge.badgeExtraOption.storeSidoLegalCode.eq(sidoRegionCode)
                        )
                        .orderBy(badge.badgePolicy.badgeLevel.desc())
                        .limit(1)
                        .fetchOne()
        );
    }


    @Override
    public List<UserBadgeResponse> findNotOptionBadgeList(Long userId, List<Long> notOptionBadge) {
        QUserBadge userBadge = QUserBadge.userBadge;
        QBadge badge = QBadge.badge;
        QBadgeCategory badgeCategory = QBadgeCategory.badgeCategory;
        QBadgeGroup badgeGroup = QBadgeGroup.badgeGroup;

        // 각 카테고리별 최고 레벨 구하기
        List<Tuple> maxLevels = queryFactory
                .select(
                        badge.badgeCategory.id,
                        badge.badgePolicy.badgeLevel.max()
                )
                .from(userBadge)
                .join(userBadge.badge, badge)
                .join(badge.badgeCategory, badgeCategory)
                .join(badgeCategory.badgeGroup, badgeGroup)
                .where(
                        userBadge.userId.eq(userId),
                        userBadge.obtainedAt.isNotNull(),
                        badgeGroup.id.in(notOptionBadge)
                )
                .groupBy(badge.badgeCategory.id)
                .fetch();

        if (maxLevels.isEmpty()) {
            return Collections.emptyList();
        }

        // 동적 where
        BooleanBuilder condition = new BooleanBuilder();
        for (Tuple tuple : maxLevels) {
            Long categoryId = tuple.get(badge.badgeCategory.id);
            Integer level = tuple.get(1, Integer.class); // index 1이 maxLevel 위치

            if(categoryId == null) {
                continue;
            }
            BooleanBuilder catCond = new BooleanBuilder().and(badge.badgeCategory.id.eq(categoryId));

            if (level == null) {
                catCond.and(badge.badgePolicy.badgeLevel.isNull());
            } else {
                catCond.and(badge.badgePolicy.badgeLevel.eq(level));
            }
            condition.or(catCond);
        }

        return queryFactory
                .select(Projections.constructor(UserBadgeResponse.class,
                        badgeGroup.id,
                        badgeCategory.id,
                        badge.id,
                        badgeCategory.badgeCategoryName,
                        badge.badgeName,
                        userBadge.accumulativeValue,
                        badge.badgeImageUrl
                ))
                .from(userBadge)
                .join(userBadge.badge, badge)
                .join(badge.badgeCategory, badgeCategory)
                .join(badgeCategory.badgeGroup, badgeGroup)
                .where(
                        userBadge.userId.eq(userId),
                        userBadge.obtainedAt.isNotNull(),
                        condition
                )
                .orderBy(
                        badgeGroup.id.asc(),
                        badgeCategory.id.asc()
                )
                .fetch();
    }


    @Override
    public List<UserBadgeResponse> findOptionBadgeList(Long userId, List<Long> optionBadge) {
        QUserBadge userBadge = QUserBadge.userBadge;
        QBadge badge = QBadge.badge;
        QBadgeCategory badgeCategory = QBadgeCategory.badgeCategory;
        QBadgeGroup badgeGroup = QBadgeGroup.badgeGroup;
        QCategory ramenCategory = QCategory.category;

        // 각 카테고리별 최고 레벨 구하기
        List<Tuple> maxLevels = queryFactory
                .select(
                        badge.badgeCategory.id,
                        badge.badgeExtraOption.ramenCategory,
                        badge.badgeExtraOption.storeSidoLegalCode,
                        badge.badgePolicy.badgeLevel.max()
                )
                .from(userBadge)
                .join(userBadge.badge, badge)
                .join(badge.badgeCategory, badgeCategory)
                .join(badgeCategory.badgeGroup, badgeGroup)
                .join(badge.badgeExtraOption.ramenCategory, ramenCategory)
                .where(
                        userBadge.userId.eq(userId),
                        userBadge.obtainedAt.isNotNull(),
                        badgeGroup.id.in(optionBadge)
                )
                .groupBy(badge.badgeCategory.id)
                .fetch();

        if (maxLevels.isEmpty()) {
            return Collections.emptyList();
        }

        // 동적 where
        BooleanBuilder condition = new BooleanBuilder();
        for (Tuple tuple : maxLevels) {
            Long categoryId = tuple.get(badge.badgeCategory.id);
            Integer level = tuple.get(1, Integer.class); // index 1이 maxLevel 위치

            if(categoryId == null) {
                continue;
            }
            BooleanBuilder catCond = new BooleanBuilder().and(badge.badgeCategory.id.eq(categoryId));

            if (level == null) {
                catCond.and(badge.badgePolicy.badgeLevel.isNull());
            } else {
                catCond.and(badge.badgePolicy.badgeLevel.eq(level));
            }
            condition.or(catCond);
        }

        return queryFactory
                .select(Projections.constructor(UserBadgeResponse.class,
                        badgeGroup.id,
                        badgeCategory.id,
                        badge.id,
                        badgeCategory.badgeCategoryName,
                        badge.badgeName,
                        userBadge.accumulativeValue,
                        badge.badgeImageUrl,
                        badge.badgeExtraOption.storeSidoLegalCode,
                        badge.badgeExtraOption.ramenCategory.name
                ))
                .from(userBadge)
                .join(userBadge.badge, badge)
                .join(badge.badgeCategory, badgeCategory)
                .join(badgeCategory.badgeGroup, badgeGroup)
                .where(
                        userBadge.userId.eq(userId),
                        userBadge.obtainedAt.isNotNull(),
                        condition
                )
                .orderBy(
                        badgeGroup.id.asc(),
                        badgeCategory.id.asc()
                )
                .fetch();
    }

}
