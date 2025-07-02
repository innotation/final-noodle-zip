package noodlezip.badge.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.badge.entity.*;
import noodlezip.mypage.dto.UserNoOptionBadgeDto;
import noodlezip.mypage.dto.UserOptionBadgeDto;
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
    public List<UserNoOptionBadgeDto> findNoOptionBadgeList(Long userId, List<Long> notOptionBadge) {
        QUserBadge userBadge = QUserBadge.userBadge;
        QBadge badge = QBadge.badge;
        QBadgeCategory badgeCategory = QBadgeCategory.badgeCategory;
        QBadgeGroup badgeGroup = QBadgeGroup.badgeGroup;

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
                        badgeCategory.id.in(notOptionBadge)
                )
                .groupBy(badge.badgeCategory.id)
                .fetch();

        if (maxLevels.isEmpty()) {
            return Collections.emptyList();
        }

        BooleanBuilder condition = new BooleanBuilder();
        for (Tuple tuple : maxLevels) {
            Long categoryId = tuple.get(badge.badgeCategory.id);
            Integer level = tuple.get(1, Integer.class);

            if (categoryId == null) {
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
                .select(Projections.constructor(UserNoOptionBadgeDto.class,
                        userBadge.id,
                        badge.id,
                        badgeCategory.id,
                        badgeGroup.id,
                        badgeCategory.badgeDescription,
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
    public List<UserOptionBadgeDto> findOptionBadgeList(Long userId, List<Long> optionBadge) {
        QUserBadge userBadge = QUserBadge.userBadge;
        QUserBadge subUserBadge = new QUserBadge("subUserBadge");
        QBadge badge = QBadge.badge;
        QBadge subBadge = new QBadge("subBadge");
        QBadgeCategory badgeCategory = QBadgeCategory.badgeCategory;
        QBadgeGroup badgeGroup = QBadgeGroup.badgeGroup;
        QCategory ramenCategory = QCategory.category;


        BooleanBuilder condition = new BooleanBuilder()
                .and(userBadge.userId.eq(userId))
                .and(userBadge.obtainedAt.isNotNull())
                .and(badgeCategory.id.in(optionBadge))
                .and(
                        JPAExpressions.selectOne()
                                .from(subUserBadge)
                                .join(subUserBadge.badge, subBadge)
                                .where(
                                        subUserBadge.userId.eq(userId),
                                        subUserBadge.obtainedAt.isNotNull(),
                                        subBadge.badgeCategory.id.eq(badge.badgeCategory.id),
                                        badge.badgeExtraOption.storeSidoLegalCode.coalesce(-1)
                                                .eq(subBadge.badgeExtraOption.storeSidoLegalCode.coalesce(-1)),
                                        badge.badgeExtraOption.ramenCategory.id.coalesce(-1)
                                                .eq(subBadge.badgeExtraOption.ramenCategory.id.coalesce(-1)),
                                        subBadge.badgePolicy.badgeLevel.gt(badge.badgePolicy.badgeLevel)
                                )
                                .notExists()
                );

        return queryFactory
                .select(Projections.constructor(UserOptionBadgeDto.class,
                        userBadge.id,
                        badge.id,
                        badgeCategory.id,
                        badgeGroup.id,
                        badge.badgeExtraOption.storeSidoLegalCode,
                        ramenCategory.name,
                        badge.badgeName,
                        userBadge.accumulativeValue,
                        badge.badgeImageUrl
                ))
                .from(userBadge)
                .join(userBadge.badge, badge)
                .leftJoin(badge.badgeCategory, badgeCategory)
                .join(badgeCategory.badgeGroup, badgeGroup)
                .leftJoin(badge.badgeExtraOption.ramenCategory, ramenCategory)
                .where(condition)
                .orderBy(
                        badgeGroup.id.asc(),
                        badge.badgeExtraOption.ramenCategory.id.asc(),
                        badge.badgeExtraOption.storeSidoLegalCode.asc()
                )
                .fetch();
    }

}
