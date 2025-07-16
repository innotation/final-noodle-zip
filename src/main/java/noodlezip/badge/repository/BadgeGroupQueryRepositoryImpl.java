package noodlezip.badge.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.badge.dto.response.BadgeGroupResponse;
import noodlezip.badge.entity.QBadgeGroup;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class BadgeGroupQueryRepositoryImpl implements BadgeGroupQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BadgeGroupResponse> getBadgeGroups() {
        QBadgeGroup badgeGroup = QBadgeGroup.badgeGroup;

        return queryFactory
                .select(Projections.constructor(BadgeGroupResponse.class,
                        badgeGroup.id,
                        badgeGroup.badgeGroupName
                ))
                .from(badgeGroup)
                .orderBy(badgeGroup.id.asc())
                .fetch();
    }

}
