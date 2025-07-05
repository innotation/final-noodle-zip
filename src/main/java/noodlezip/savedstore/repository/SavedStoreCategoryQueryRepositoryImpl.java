package noodlezip.savedstore.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;
import noodlezip.savedstore.entity.QSavedStoreCategory;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SavedStoreCategoryQueryRepositoryImpl implements SavedStoreCategoryQueryRepository {

    private final JPAQueryFactory queryFactory;

    public List<SavedStoreCategoryResponse> findUserSaveCategoryListForSearch(long userId, boolean isOwner) {
        QSavedStoreCategory savedStoreCategory = QSavedStoreCategory.savedStoreCategory;

        BooleanBuilder where = new BooleanBuilder();
        where.and(savedStoreCategory.user.id.eq(userId));
        if (!isOwner) {
            where.and(savedStoreCategory.isPublic.eq(true));
        }

        return queryFactory
                .select(Projections.constructor(SavedStoreCategoryResponse.class,
                        savedStoreCategory.id,
                        savedStoreCategory.categoryName,
                        Expressions.constant(false)
                ))
                .from(savedStoreCategory)
                .where(where)
                .orderBy(savedStoreCategory.createdAt.desc())
                .fetch();
    }

    @Override
    public List<SavedStoreCategoryResponse> findUserSavedCategoryList(long userId) {
        QSavedStoreCategory savedStoreCategory = QSavedStoreCategory.savedStoreCategory;

        return queryFactory
                .select(Projections.constructor(SavedStoreCategoryResponse.class,
                        savedStoreCategory.id,
                        savedStoreCategory.categoryName,
                        savedStoreCategory.isPublic
                ))
                .from(savedStoreCategory)
                .where(
                        savedStoreCategory.user.id.eq(userId)
                )
                .orderBy(savedStoreCategory.createdAt.desc())
                .fetch();
    }

}
