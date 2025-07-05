package noodlezip.savedstore.repository;

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

    public List<SavedStoreCategoryResponse> findUserSaveCategoryList(long userId) {
        QSavedStoreCategory savedStoreCategory = QSavedStoreCategory.savedStoreCategory;

        return queryFactory
                .select(Projections.constructor(SavedStoreCategoryResponse.class,
                        savedStoreCategory.id,
                        savedStoreCategory.categoryName,
                        Expressions.constant(false)
                ))
                .from(savedStoreCategory)
                .where(savedStoreCategory.user.id.eq(userId))
                .orderBy(savedStoreCategory.createdAt.desc())
                .fetch();
    }

}
