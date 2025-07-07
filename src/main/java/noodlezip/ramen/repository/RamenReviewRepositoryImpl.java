package noodlezip.ramen.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.ramen.entity.QRamenReview;
import noodlezip.store.dto.ReviewSummaryDto;
import noodlezip.store.dto.StoreReviewDto;
import noodlezip.store.entity.QMenu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RamenReviewRepositoryImpl implements RamenReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreReviewDto> findReviewsByStoreId(Long storeId, Pageable pageable) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;

        List<StoreReviewDto> content = queryFactory
                .select(Projections.constructor(
                        StoreReviewDto.class,
                        review.id,
                        review.communityId,
                        menu.id,
                        menu.menuName,
                        review.noodleThickness,
                        review.noodleTexture,
                        review.noodleBoilLevel,
                        review.soupTemperature,
                        review.soupSaltiness,
                        review.soupSpicinessLevel,
                        review.soupOiliness,
                        review.soupFlavorKeywords,
                        review.content,
                        review.reviewImageUrl,
                        review.isReceiptReview
                ))
                .from(review)
                .join(review.menu, menu)
                .where(menu.store.id.eq(storeId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // 카운트 쿼리
        Long count = queryFactory
                .select(review.count())
                .from(review)
                .join(review.menu, menu)
                .where(menu.store.id.eq(storeId))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public ReviewSummaryDto getSummaryByStoreId(Long storeId) {
        // QRamenReview 사용
        QRamenReview review = QRamenReview.ramenReview;

        ReviewSummaryDto summary = queryFactory
                .select(Projections.fields(
                        ReviewSummaryDto.class,
                        review.noodleThickness.avg().as("noodleThickness"),
                        review.noodleTexture.avg().as("noodleTexture"),
                        review.noodleBoilLevel.avg().as("noodleBoilLevel"),
                        review.soupTemperature.avg().as("soupTemperature"),
                        review.soupSaltiness.avg().as("soupSaltiness"),
                        review.soupSpicinessLevel.avg().as("soupSpicinessLevel"),
                        review.soupOiliness.avg().as("soupOiliness"),
                        review.id.count().intValue().as("totalCount"),
                        review.noodleThickness.avg()
                                .add(review.noodleTexture.avg())
                                .add(review.noodleBoilLevel.avg())
                                .add(review.soupTemperature.avg())
                                .add(review.soupSaltiness.avg())
                                .add(review.soupSpicinessLevel.avg())
                                .add(review.soupOiliness.avg())
                                .divide(7.0).as("overall")
                ))
                .from(review)
                .where(review.menu.store.id.eq(storeId))
                .fetchOne();

        return summary;
    }
}
