package noodlezip.savedstore.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.mypage.dto.request.savedstore.SavedStoreCategoryFilterRequest;
import noodlezip.mypage.dto.response.savedstore.SavedStoreResponse;
import noodlezip.mypage.dto.response.savedstore.StoreLocationResponse;
import noodlezip.savedstore.entity.QSavedStore;
import noodlezip.savedstore.entity.QSavedStoreCategory;
import noodlezip.store.entity.QStore;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                .where(where)
                .fetchOne();

        return new PageImpl<>(content, pageable, total == null ? 0 : total);
    }

    @Override
    public List<StoreLocationResponse> getStoreLocationList(Long userId,
                                                            List<Long> categoryIdList,
                                                            boolean isOwner
    ) {
        QSavedStore savedStore = QSavedStore.savedStore;
        QSavedStoreCategory savedStoreCategory = QSavedStoreCategory.savedStoreCategory;

        BooleanBuilder where = new BooleanBuilder();
        where.and(savedStore.user.id.eq(userId));
        where.and(savedStoreCategory.id.in(categoryIdList));
        if (!isOwner) {
            where.and(savedStoreCategory.isPublic.eq(true));
        }

        return queryFactory
                .select(Projections.constructor(StoreLocationResponse.class,
                        savedStore.store.id,
                        savedStore.id,
                        savedStore.location.storeLat,
                        savedStore.location.storeLng
                ))
                .from(savedStore)
                .join(savedStore.saveStoreCategory, savedStoreCategory)
                .where(where)
                .fetch();
    }

}
