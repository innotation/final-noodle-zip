package noodlezip.ramen.repository;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.community.entity.CommunityActiveStatus;
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
        noodlezip.store.entity.QStore store = noodlezip.store.entity.QStore.store;

        List<StoreReviewDto> content = queryFactory
                .select(Projections.fields(
                        StoreReviewDto.class,
                        review.id.as("id"),
                        review.communityId.as("communityId"),
                        menu.id.as("menuId"),
                        menu.menuName.as("menuName"),
                        review.noodleThickness.as("noodleThickness"),
                        review.noodleTexture.as("noodleTexture"),
                        review.noodleBoilLevel.as("noodleBoilLevel"),
                        review.soupDensity.as("soupDensity"),
                        review.soupTemperature.as("soupTemperature"),
                        review.soupSaltiness.as("soupSaltiness"),
                        review.soupSpicinessLevel.as("soupSpicinessLevel"),
                        review.soupOiliness.as("soupOiliness"),
                        review.soupFlavorKeywords.as("soupFlavorKeywords"),
                        review.content.as("content"),
                        review.reviewImageUrl.as("reviewImageUrl"),
                        review.isReceiptReview.as("isReceiptReview"),
                        board.user.userName.as("userName"),
                        board.user.id.as("userId"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),
                        store.id.as("storeId"),
                        store.storeName.as("storeName"),
                        store.address.as("storeAddress")
                ))
                .from(review)
                .join(review.menu, menu)
                .join(menu.store, store)
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
        QRamenReview review = QRamenReview.ramenReview;
        QBoard board = QBoard.board;

        ReviewSummaryDto summary = queryFactory
                .select(Projections.fields(
                        ReviewSummaryDto.class,
                        review.noodleThickness.avg().as("noodleThickness"),
                        review.noodleTexture.avg().as("noodleTexture"),
                        review.noodleBoilLevel.avg().as("noodleBoilLevel"),
                        review.soupDensity.avg().as("soupDensity"),
                        review.soupTemperature.avg().as("soupTemperature"),
                        review.soupSaltiness.avg().as("soupSaltiness"),
                        review.soupSpicinessLevel.avg().as("soupSpicinessLevel"),
                        review.soupOiliness.avg().as("soupOiliness"),
                        review.id.count().intValue().as("totalCount"),
                        review.noodleThickness.avg()
                                .add(review.noodleTexture.avg())
                                .add(review.noodleBoilLevel.avg())
                                .add(review.soupDensity.avg())
                                .add(review.soupTemperature.avg())
                                .add(review.soupSaltiness.avg())
                                .add(review.soupSpicinessLevel.avg())
                                .add(review.soupOiliness.avg())
                                .divide(8.0).as("overall")
                ))
                .from(review)
                .join(board).on(review.communityId.eq(board.id))
                .where(review.menu.store.id.eq(storeId)
                        .and(board.postStatus.eq(CommunityActiveStatus.POSTED)))
                .fetchOne();

        return summary;
    }

    @Override
    public ReviewSummaryDto getSummaryByStoreIdAndMenuName(Long storeId, String menuName) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QBoard board = QBoard.board;

        ReviewSummaryDto summary = queryFactory
                .select(Projections.fields(
                        ReviewSummaryDto.class,
                        review.noodleThickness.avg().as("noodleThickness"),
                        review.noodleTexture.avg().as("noodleTexture"),
                        review.noodleBoilLevel.avg().as("noodleBoilLevel"),
                        review.soupDensity.avg().as("soupDensity"),
                        review.soupTemperature.avg().as("soupTemperature"),
                        review.soupSaltiness.avg().as("soupSaltiness"),
                        review.soupSpicinessLevel.avg().as("soupSpicinessLevel"),
                        review.soupOiliness.avg().as("soupOiliness"),
                        review.id.count().intValue().as("totalCount"),
                        review.noodleThickness.avg()
                                .add(review.noodleTexture.avg())
                                .add(review.noodleBoilLevel.avg())
                                .add(review.soupDensity.avg())
                                .add(review.soupTemperature.avg())
                                .add(review.soupSaltiness.avg())
                                .add(review.soupSpicinessLevel.avg())
                                .add(review.soupOiliness.avg())
                                .divide(8.0).as("overall")
                ))
                .from(review)
                .join(review.menu, menu)
                .join(board).on(review.communityId.eq(board.id))
                .where(review.menu.store.id.eq(storeId)
                        .and(board.postStatus.eq(CommunityActiveStatus.POSTED))
                        .and(menu.menuName.eq(menuName)))
                .fetchOne();

        return summary;
    }

    @Override
    public boolean existsByOcrKeyHash(String ocrKeyHash) {
        QBoard board = QBoard.board;
        Integer result = queryFactory
                .selectOne()
                .from(board)
                .where(board.ocrKeyHash.eq(ocrKeyHash))
                .fetchFirst();

        return result != null;
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

    @Override
    public Page<StoreReviewDto> findReviewsByUserId(Long userId, Pageable pageable) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QBoard board = QBoard.board;
        noodlezip.store.entity.QStore store = noodlezip.store.entity.QStore.store;

        List<StoreReviewDto> content = queryFactory
                .select(Projections.fields(
                        StoreReviewDto.class,
                        review.id.as("id"),
                        review.communityId.as("communityId"),
                        menu.id.as("menuId"),
                        menu.menuName.as("menuName"),
                        review.noodleThickness.as("noodleThickness"),
                        review.noodleTexture.as("noodleTexture"),
                        review.noodleBoilLevel.as("noodleBoilLevel"),
                        review.soupDensity.as("soupDensity"),
                        review.soupTemperature.as("soupTemperature"),
                        review.soupSaltiness.as("soupSaltiness"),
                        review.soupSpicinessLevel.as("soupSpicinessLevel"),
                        review.soupOiliness.as("soupOiliness"),
                        review.soupFlavorKeywords.as("soupFlavorKeywords"),
                        review.content.as("content"),
                        review.reviewImageUrl.as("reviewImageUrl"),
                        review.isReceiptReview.as("isReceiptReview"),
                        board.user.userName.as("userName"),
                        board.user.id.as("userId"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),
                        store.id.as("storeId"),
                        store.storeName.as("storeName"),
                        store.address.as("storeAddress"),
                        store.storeLegalCode.as("storeLegalCode"),
                        menu.category.categoryName.as("categoryName")
                ))
                .from(review)
                .join(review.menu, menu)
                .join(menu.store, store)
                .join(board).on(review.communityId.eq(board.id))
                .where(board.user.id.eq(userId))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long count = queryFactory
                .select(review.count())
                .from(review)
                .join(board).on(review.communityId.eq(board.id))
                .where(board.user.id.eq(userId))
                .fetchOne();

        return new PageImpl<>(content, pageable, count);
    }

    @Override
    public List<Long> findIdsByBoardId(Long boardId) {
        QRamenReview ramenReview = QRamenReview.ramenReview;

        return queryFactory
                .select(ramenReview.id)
                .from(ramenReview)
                .where(ramenReview.communityId.eq(boardId))
                .fetch();
    }

//    @Override
//    public List<StoreReviewDto> findReviewsByBoardId(Long boardId) {
//        QRamenReview review = QRamenReview.ramenReview;
//        QMenu menu = QMenu.menu;
//        QBoard board = QBoard.board;
//        noodlezip.store.entity.QStore store = noodlezip.store.entity.QStore.store;
//
//        List<StoreReviewDto> result = queryFactory
//                .select(Projections.fields(
//                        StoreReviewDto.class,
//                        review.id.as("id"),
//                        review.communityId.as("communityId"),
//                        menu.id.as("menuId"),
//                        menu.menuName.as("menuName"),
//                        review.noodleThickness.as("noodleThickness"),
//                        review.noodleTexture.as("noodleTexture"),
//                        review.noodleBoilLevel.as("noodleBoilLevel"),
//                        review.soupDensity.as("soupDensity"),
//                        review.soupTemperature.as("soupTemperature"),
//                        review.soupSaltiness.as("soupSaltiness"),
//                        review.soupSpicinessLevel.as("soupSpicinessLevel"),
//                        review.soupOiliness.as("soupOiliness"),
//                        review.soupFlavorKeywords.as("soupFlavorKeywords"),
//                        review.content.as("content"),
//                        review.reviewImageUrl.as("reviewImageUrl"),
//                        review.isReceiptReview.as("isReceiptReview"),
//                        board.user.userName.as("userName"),
//                        board.user.id.as("userId"),
//                        board.createdAt.as("createdAt"),
//                        board.updatedAt.as("updatedAt"),
//                        store.id.as("storeId"),
//                        store.storeName.as("storeName"),
//                        store.address.as("storeAddress")
//                ))
//                .from(review)
//                .join(review.menu, menu)
//                .join(menu.store, store)
//                .join(board).on(review.communityId.eq(boardId))
//                .distinct()
//                .where(review.communityId.eq(boardId))
//                .fetch();
//
//        return result;
//    }

    @Override
    public List<StoreReviewDto> findReviewsByBoardId(Long boardId) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QBoard board = QBoard.board; // QBoard가 Community 엔티티를 나타낸다고 가정
        noodlezip.store.entity.QStore store = noodlezip.store.entity.QStore.store;
        // QUser user = QUser.user; // User 엔티티가 있다면 QUser 추가 (Board에 User가 포함되어 있다면 필요 없을 수 있음)

        List<StoreReviewDto> result = queryFactory
                .select(Projections.fields(
                        StoreReviewDto.class,
                        review.id.as("id"),
                        review.communityId.as("communityId"), // 리뷰의 communityId
                        menu.id.as("menuId"),
                        menu.menuName.as("menuName"),
                        review.noodleThickness.as("noodleThickness"),
                        review.noodleTexture.as("noodleTexture"),
                        review.noodleBoilLevel.as("noodleBoilLevel"),
                        review.soupDensity.as("soupDensity"),
                        review.soupTemperature.as("soupTemperature"),
                        review.soupSaltiness.as("soupSaltiness"),
                        review.soupSpicinessLevel.as("soupSpicinessLevel"),
                        review.soupOiliness.as("soupOiliness"),
                        review.soupFlavorKeywords.as("soupFlavorKeywords"),
                        review.content.as("content"),
                        review.reviewImageUrl.as("reviewImageUrl"),
                        review.isReceiptReview.as("isReceiptReview"),
                        board.user.userName.as("userName"),
                        board.user.id.as("userId"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),
                        store.id.as("storeId"),
                        store.storeName.as("storeName"),
                        store.address.as("storeAddress")
                ))
                .from(review)
                .join(review.menu, menu)
                .join(menu.store, store)
                .join(board).on(review.communityId.eq(board.id))
                .distinct()
                .where(review.communityId.eq(boardId))
                .fetch();

        return result;
    }


    @Override
    public void deleteByBoardId(Long boardId) {
        QRamenReview ramenReview = QRamenReview.ramenReview;

        queryFactory
                .delete(ramenReview)
                .where(ramenReview.communityId.eq(boardId))
                .execute();

    }

    @Override
    public List<StoreReviewDto> findAllReviewsByUserId(Long userId) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QBoard board = QBoard.board;
        noodlezip.store.entity.QStore store = noodlezip.store.entity.QStore.store;

        return queryFactory
                .select(Projections.fields(
                        StoreReviewDto.class,
                        review.id.as("id"),
                        review.communityId.as("communityId"),
                        menu.id.as("menuId"),
                        menu.menuName.as("menuName"),
                        review.noodleThickness.as("noodleThickness"),
                        review.noodleTexture.as("noodleTexture"),
                        review.noodleBoilLevel.as("noodleBoilLevel"),
                        review.soupDensity.as("soupDensity"),
                        review.soupTemperature.as("soupTemperature"),
                        review.soupSaltiness.as("soupSaltiness"),
                        review.soupSpicinessLevel.as("soupSpicinessLevel"),
                        review.soupOiliness.as("soupOiliness"),
                        review.soupFlavorKeywords.as("soupFlavorKeywords"),
                        review.content.as("content"),
                        review.reviewImageUrl.as("reviewImageUrl"),
                        review.isReceiptReview.as("isReceiptReview"),
                        board.user.userName.as("userName"),
                        board.user.id.as("userId"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),
                        store.id.as("storeId"),
                        store.storeName.as("storeName"),
                        store.address.as("storeAddress"),
                        store.storeLegalCode.as("storeLegalCode"),
                        menu.category.categoryName.as("categoryName")
                ))
                .from(review)
                .join(review.menu, menu)
                .join(menu.store, store)
                .join(board).on(review.communityId.eq(board.id))
                .where(board.user.id.eq(userId))
                .fetch();
    }
}
