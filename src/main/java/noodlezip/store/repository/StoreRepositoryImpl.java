package noodlezip.store.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.ramen.entity.*;
import noodlezip.store.entity.QStoreExtraTopping;
import noodlezip.search.dto.SearchFilterDto;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.store.entity.*;
import noodlezip.store.status.ApprovalStatus;
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
                lng, lat, store.storeLng, store.storeLat
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
                        store.storeLat,
                        store.storeLng,
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

    @Override
    public Page<SearchStoreDto> searchStoresByFilter(SearchFilterDto filter, Pageable pageable) {
        QStore store = QStore.store;
        QMenu menu = QMenu.menu;
        QCategory Category = QCategory.category;
        QRamenSoup ramenSoup = QRamenSoup.ramenSoup;
        QRamenTopping ramenTopping = QRamenTopping.ramenTopping;
        QStoreExtraTopping storeExtraTopping = QStoreExtraTopping.storeExtraTopping;
        QTopping topping = QTopping.topping;

        // 거리 계산
        NumberExpression<Double> distanceExpr = Expressions.numberTemplate(
                Double.class,
                "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))",
                filter.getLng(), filter.getLat(), store.storeLng, store.storeLat
        );

        BooleanBuilder builder = new BooleanBuilder();

        // 운영중인 매장
        builder.and(store.approvalStatus.eq(ApprovalStatus.APPROVED));

        // 라멘 카테고리
        if (filter.getRamenCategory() != null && !filter.getRamenCategory().isEmpty()) {
            builder.and(Category.categoryName.in(filter.getRamenCategory()));
        }

        // 육수
        if (filter.getRamenSoup() != null && !filter.getRamenSoup().isEmpty()) {
            builder.and(ramenSoup.soupName.in(filter.getRamenSoup()));
        }

        // 지역
        if (filter.getRegion() != null && !filter.getRegion().isEmpty()) {
            BooleanBuilder regionBuilder = new BooleanBuilder();
            for (String region : filter.getRegion()) {
                regionBuilder.or(store.address.containsIgnoreCase(region));
            }
            builder.and(regionBuilder);
        }

        // 토핑 (기본 OR 추가)
        if (filter.getTopping() != null && !filter.getTopping().isEmpty()) {
            BooleanBuilder toppingBuilder = new BooleanBuilder();

            // 기본 토핑 조건
            toppingBuilder.or(topping.toppingName.in(filter.getTopping())
                    .and(topping.isActive.isTrue())
            );

            // 추가 토핑 조건
            toppingBuilder.or(
                    JPAExpressions.selectOne()
                            .from(storeExtraTopping)
                            .join(topping).on(topping.id.eq(storeExtraTopping.id))
                            .where(
                                    storeExtraTopping.storeId.id.eq(store.id)
                                            .and(topping.toppingName.in(filter.getTopping()))
                                            .and(topping.isActive.isTrue())
                            )
                            .exists()
            );

            builder.and(toppingBuilder);
        }

        // 본 쿼리
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
                        store.storeLat,
                        store.storeLng,
                        distanceExpr.as("distance")
                ))
                .from(store)
                .join(menu).on(menu.id.eq(store.id))
                .join(Category).on(menu.category.id.eq(Category.id))
                .join(ramenSoup).on(menu.ramenSoup.id.eq(ramenSoup.id))
                .leftJoin(ramenTopping).on(ramenTopping.toppingId.menuId.eq(menu.id))
                .leftJoin(topping).on(topping.id.eq(ramenTopping.toppingId.toppingId))
                .where(builder)
                .distinct()
                .orderBy(distanceExpr.asc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(store.countDistinct())
                .from(store)
                .join(menu).on(menu.id.eq(store.id))
                .join(Category).on(menu.category.id.eq(Category.id))
                .join(ramenSoup).on(menu.ramenSoup.id.eq(ramenSoup.id))
                .leftJoin(ramenTopping).on(ramenTopping.toppingId.menuId.eq(menu.id))
                .leftJoin(topping).on(topping.id.eq(ramenTopping.toppingId.toppingId))
                .where(builder)
                .fetchOne();

        return new PageImpl<>(content, pageable, total);
    }

}
