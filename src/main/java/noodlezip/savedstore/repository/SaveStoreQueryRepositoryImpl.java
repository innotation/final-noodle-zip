package noodlezip.savedstore.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.SavedStoreMapResponse;
import noodlezip.mypage.dto.response.savedstore.SavedStoreResponse;
import noodlezip.savedstore.entity.QSavedStore;
import noodlezip.savedstore.entity.QSavedStoreCategory;
import noodlezip.search.dto.SearchStoreDto;
import noodlezip.store.entity.QStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Repository
public class SaveStoreQueryRepositoryImpl implements SaveStoreQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<SavedStoreResponse> findSavedStoreByCategoryWithPaging(Long userId,
                                                                       SavedStoreCategoryFilterRequest filter,
                                                                       boolean isOwner,
                                                                       Pageable pageable
    ) {
        QSavedStore savedStore = QSavedStore.savedStore;
        QSavedStoreCategory savedStoreCategory = QSavedStoreCategory.savedStoreCategory;
        QStore store = QStore.store;

        List<Long> categoryIdList = filter.getCategoryIdList();
        boolean isFilterCategory = !filter.isAllCategory()
                && categoryIdList != null
                && !categoryIdList.isEmpty();

        BooleanBuilder where = new BooleanBuilder();
        where.and(savedStore.user.id.eq(userId));
        if (!isOwner) {
            where.and(savedStoreCategory.isPublic.eq(true));
        }
        if (isFilterCategory) {
            where.and(savedStoreCategory.id.in(categoryIdList));
        }

        List<SavedStoreResponse> content = queryFactory
                .select(Projections.constructor(SavedStoreResponse.class,
                        savedStore.id,
                        savedStoreCategory.id,
                        store.id,
                        store.storeName,
                        store.address,
                        store.storeMainImageUrl,
                        savedStore.memo
                ))
                .from(savedStore)
                .join(savedStore.saveStoreCategory, savedStoreCategory)
                .join(savedStore.store, store)
                .where(where)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(savedStore.count())
                .from(savedStore)
                .join(savedStore.saveStoreCategory, savedStoreCategory)
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public SavedStoreMapResponse getStoreLocationList(Long userId,
                                                      List<Long> categoryIdList,
                                                      boolean isOwner
    ) {
        QSavedStoreCategory savedStoreCategory = QSavedStoreCategory.savedStoreCategory;
        QSavedStore savedStore = QSavedStore.savedStore;
        QStore store = QStore.store;

        BooleanBuilder where = new BooleanBuilder();
        where.and(savedStore.user.id.eq(userId));
        where.and(savedStoreCategory.id.in(categoryIdList));
        if (!isOwner) {
            where.and(savedStoreCategory.isPublic.eq(true));
        }

        List<Tuple> resultList = queryFactory
                .select(
                        savedStore.id,
                        savedStoreCategory.id,
                        store.id,
                        store.storeName,
                        store.address,
                        store.storeMainImageUrl,
                        savedStore.memo,
                        store.storeLat,
                        store.storeLng
                )
                .from(savedStore)
                .join(savedStore.saveStoreCategory, savedStoreCategory)
                .join(savedStore.store, store)
                .where(where)
                .fetch();

        Map<Integer, List<SavedStoreResponse>> grouped = resultList.stream()
                .collect(Collectors.groupingBy(
                        tuple -> Objects.requireNonNull(tuple.get(savedStoreCategory.id)).intValue(),
                        Collectors.mapping(
                                tuple -> SavedStoreResponse.builder()
                                        .savedStoreId(tuple.get(savedStore.id))
                                        .saveStoreCategoryId(tuple.get(savedStoreCategory.id))
                                        .storeId(tuple.get(store.id))
                                        .storeName(tuple.get(store.storeName))
                                        .address(tuple.get(store.address))
                                        .storeMainImageUrl(tuple.get(store.storeMainImageUrl))
                                        .memo(tuple.get(savedStore.memo))
                                        .storeLat(tuple.get(store.storeLat))
                                        .storeLng(tuple.get(store.storeLng))
                                        .build(),
                                Collectors.toList()
                        )
                ));

        return SavedStoreMapResponse.builder()
                .locationListByCategoryId(grouped)
                .build();
    }

}
