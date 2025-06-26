package noodlezip.store.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.store.entity.QStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class StoreRepositoryImpl implements StoreRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SearchStoreDto> findStoreOrderByDistance(double lat, double lng, Pageable pageable) {
        QStore store = QStore.store;

        NumberExpression<Double> distanceExpr = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))",
                lng, lat, store.xAxis, store.yAxis
        );

        List<SearchStoreDto> content = queryFactory
                .select(Projections.constructor(SearchStoreDto.class,
                        store.id,
                        store.storeName,
                        store.address,
                        store.phone,
                        store.isLocalCard,
                        store.isChildAllowed,
                        store.hasParking,
                        store.ownerComment,
                        store.storeMainImageUrl,
                        store.xAxis,
                        store.yAxis,
                        distanceExpr.as("distance")
                ))
                .from(store)
                .orderBy(distanceExpr.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        log.info("{}", content.toString());

        long total = queryFactory
                .select(store.count())
                .from(store)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }
}
