package noodlezip.ramen.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.ramen.entity.QRamenReview;
import noodlezip.store.dto.ReviewSummaryDto;
import noodlezip.store.dto.StoreReviewDto;
import noodlezip.store.entity.QMenu;
import noodlezip.community.entity.QBoard;
import noodlezip.ramen.entity.QCategory;
import noodlezip.ramen.entity.QRamenSoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class RamenReviewRepositoryImpl implements RamenReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StoreReviewDto> findReviewsByStoreId(Long storeId, Pageable pageable) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QBoard board = QBoard.board;

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
                        review.isReceiptReview,
                        board.user.userName, // 작성자 이름
                        board.user.id        // 작성자 id
                ))
                .from(review)
                .join(review.menu, menu)
                .join(board).on(review.communityId.eq(board.id))
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

    @Override
    public ReviewSummaryDto getSummaryByStoreIdAndMenuName(Long storeId, String menuName) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;

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
                .join(review.menu, menu)
                .where(review.menu.store.id.eq(storeId).and(menu.menuName.eq(menuName)))
                .fetchOne();

        return summary;
    }

    @Override
    public boolean existsByOcrKeyHash(String ocrKeyHash) {
        QRamenReview review = QRamenReview.ramenReview;
        Long count = queryFactory
                .select(review.count())
                .from(review)
                .where(review.ocrKeyHash.eq(ocrKeyHash))
                .fetchOne();

        return count > 0;
    }

    @Override
    public Map<String, Long> getReviewCountByCategory() {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QCategory category = QCategory.category;

        List<Tuple> results = queryFactory
                .select(category.categoryName, review.count())
                .from(review)
                .join(review.menu, menu)
                .join(menu.category, category)
                .groupBy(category.categoryName)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(category.categoryName),
                        tuple -> tuple.get(review.count())
                ));
    }

    @Override
    public Map<String, Long> getReviewCountBySoup() {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QRamenSoup soup = QRamenSoup.ramenSoup;

        List<Tuple> results = queryFactory
                .select(soup.soupName, review.count())
                .from(review)
                .join(review.menu, menu)
                .join(menu.ramenSoup, soup)
                .groupBy(soup.soupName)
                .fetch();

        return results.stream()
                .collect(Collectors.toMap(
                        tuple -> tuple.get(soup.soupName),
                        tuple -> tuple.get(review.count())
                ));
    }

    @Override
    public Page<StoreReviewDto> findReviewsByTag(String tag, String type, Pageable pageable) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QBoard board = QBoard.board;
        QCategory category = QCategory.category;
        QRamenSoup soup = QRamenSoup.ramenSoup;

        List<StoreReviewDto> content;
        Long count;

        if ("category".equals(type)) {
            // 카테고리로 필터링
            content = queryFactory
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
                            review.isReceiptReview,
                            board.user.userName,
                            board.user.id
                    ))
                    .from(review)
                    .join(review.menu, menu)
                    .join(menu.category, category)
                    .join(board).on(review.communityId.eq(board.id))
                    .where(category.categoryName.eq(tag))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            count = queryFactory
                    .select(review.count())
                    .from(review)
                    .join(review.menu, menu)
                    .join(menu.category, category)
                    .where(category.categoryName.eq(tag))
                    .fetchOne();
        } else {
            // 육수로 필터링
            content = queryFactory
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
                            review.isReceiptReview,
                            board.user.userName,
                            board.user.id
                    ))
                    .from(review)
                    .join(review.menu, menu)
                    .join(menu.ramenSoup, soup)
                    .join(board).on(review.communityId.eq(board.id))
                    .where(soup.soupName.eq(tag))
                    .offset(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .fetch();

            count = queryFactory
                    .select(review.count())
                    .from(review)
                    .join(review.menu, menu)
                    .join(menu.ramenSoup, soup)
                    .where(soup.soupName.eq(tag))
                    .fetchOne();
        }

        return new PageImpl<>(content, pageable, count);
    }
}
