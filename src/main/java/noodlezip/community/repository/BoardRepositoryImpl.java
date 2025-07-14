package noodlezip.community.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QBean;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.CategoryCountDto;
import noodlezip.community.dto.PopularTagDto;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.entity.QBoard;
import noodlezip.user.entity.QUser;
import noodlezip.ramen.entity.QRamenReview;
import noodlezip.store.entity.QMenu;
import noodlezip.ramen.entity.QCategory;
import noodlezip.ramen.entity.QRamenSoup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static noodlezip.community.entity.QBoard.board;
import static noodlezip.user.entity.QUser.user;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    private QBean<BoardRespDto> getBoardRespDtoProjection() {

        return Projections.fields(BoardRespDto.class,
                board.id.as("boardId"),
                board.user.id.as("userId"),
                board.user.userName.as("userName"),
                board.user.profileImageUrl.as("userProfileImageUrl"),
                board.title.as("title"),
                board.content.as("content"),
                board.communityType.as("communityType"),
                board.postStatus.as("postStatus"),
                board.likesCount.as("likesCount"),
                board.viewsCount.as("viewsCount"),
                board.createdAt.as("createdAt"),
                board.updatedAt.as("updatedAt"),
                board.imageUrl.as("imageUrl")
        );
    }

    @Override
    public Optional<BoardRespDto> findBoardByBoardIdWithUser(Long boardId) {
        QBoard board = QBoard.board;
        QUser user = QUser.user;

        BoardRespDto result = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .leftJoin(board.user, user)
                .where(
                        board.id.eq(boardId).and(board.postStatus.eq(CommunityActiveStatus.POSTED))
                )
                .fetchOne();
        return Optional.ofNullable(result);
    }

    @Override
    public Page<BoardRespDto> findBoardWithPagination(Pageable pageable) {

        List<BoardRespDto> results = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .leftJoin(board.user, user)
                .where(
                        board.postStatus.eq(CommunityActiveStatus.POSTED)
                )
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.postStatus.eq(CommunityActiveStatus.POSTED)
                )
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);


        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<BoardRespDto> findBoardByCommunityTypeWithPagination(String category, Pageable pageable) {

        List<BoardRespDto> results = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .leftJoin(board.user, user)
                .where(
                        board.postStatus.eq(CommunityActiveStatus.POSTED).and(board.communityType.eq(category))
                )
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.postStatus.eq(CommunityActiveStatus.POSTED).and(board.communityType.eq(category))
                )
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);


        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<BoardRespDto> findBoardByWriterWithPagination(Long userId, Pageable pageable) {

        List<BoardRespDto> results = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .leftJoin(board.user, user)
                .where(
                        board.user.id.eq(userId)
                )
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .where(
                        board.user.id.eq(userId)
                )
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);


        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<BoardRespDto> findBoardByCommunityTypeAndKeywordWithPagination(String category, String keyword, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(board.postStatus.eq(CommunityActiveStatus.POSTED));

        builder.and(board.communityType.eq(category));

        if (keyword != null && !keyword.trim().isEmpty()) {
            String likeKeyword = "%" + keyword.trim() + "%";
            builder.and(board.title.like(likeKeyword).or(board.content.like(likeKeyword)));
        }

        List<BoardRespDto> results = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .leftJoin(board.user, user)
                .where(builder)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .where(builder)
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<BoardRespDto> findBoardByKeywordWithPagination(String keyword, Pageable pageable) {

        BooleanBuilder builder = new BooleanBuilder();

        builder.and(board.postStatus.eq(CommunityActiveStatus.POSTED));


        if (keyword != null && !keyword.trim().isEmpty()) {
            String likeKeyword = "%" + keyword.trim() + "%";
            builder.and(board.title.like(likeKeyword).or(board.content.like(likeKeyword)));
        }
        List<BoardRespDto> results = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .leftJoin(board.user, user)
                .where(builder)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .where(builder)
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }

    @Override
    public Page<BoardRespDto> findBoardsByIdsAndStatusPostedWithPaging(List<Long> boardIds, Pageable pageable) {
        if (boardIds == null || boardIds.isEmpty()) {
            return new PageImpl<>(List.of(), pageable, 0);
        }

        List<BoardRespDto> content = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .join(board.user, user)
                .where(board.id.in(boardIds)
                        .and(board.postStatus.eq(CommunityActiveStatus.POSTED)))
                .orderBy(board.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(board.count())
                .from(board)
                .where(board.id.in(boardIds).and(board.postStatus.eq(CommunityActiveStatus.POSTED)))
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);

        return new PageImpl<>(content, pageable, totalCount);
    }

    @Override
    public Long increaseViewCount(Long boardId, Long viewCount) {

        Long result = queryFactory
                .update(board)
                .set(board.viewsCount, board.viewsCount.add(viewCount))
                .where(board.id.eq(boardId))
                .execute();
        return result;
    }

    @Override
    public List<CategoryCountDto> findCategoryCounts() {
        return queryFactory
                .select(Projections.constructor(CategoryCountDto.class,
                        board.communityType,
                        board.count()))
                .from(board)
                .where(board.postStatus.eq(CommunityActiveStatus.POSTED))
                .groupBy(board.communityType)
                .orderBy(board.communityType.asc())
                .fetch();
    }

    @Override
    public List<PopularTagDto> findPopularTags() {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QCategory category = QCategory.category;
        QRamenSoup soup = QRamenSoup.ramenSoup;

        List<PopularTagDto> popularTags = new ArrayList<>();

        // 카테고리별 리뷰 개수 조회
        List<PopularTagDto> categoryTags = queryFactory
                .select(Projections.constructor(PopularTagDto.class,
                        category.categoryName,
                        Expressions.constant("category"),
                        board.count()))
                .from(board)
                .join(review).on(review.communityId.eq(board.id))
                .join(review.menu, menu)
                .join(menu.category, category)
                .where(board.postStatus.eq(CommunityActiveStatus.POSTED)
                        .and(board.communityType.eq("review")))
                .groupBy(category.categoryName)
                .fetch();

        // 육수별 리뷰 개수 조회
        List<PopularTagDto> soupTags = queryFactory
                .select(Projections.constructor(PopularTagDto.class,
                        soup.soupName,
                        Expressions.constant("soup"),
                        board.count()))
                .from(board)
                .join(review).on(review.communityId.eq(board.id))
                .join(review.menu, menu)
                .join(menu.ramenSoup, soup)
                .where(board.postStatus.eq(CommunityActiveStatus.POSTED)
                        .and(board.communityType.eq("review")))
                .groupBy(soup.soupName)
                .fetch();

        popularTags.addAll(categoryTags);
        popularTags.addAll(soupTags);

        // 인기 순으로 정렬 (리뷰 개수로 정렬)
        return popularTags.stream()
                .sorted((a, b) -> b.getCount().compareTo(a.getCount()))
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    public Page<BoardRespDto> findReviewBoardsByTag(String tag, String type, Pageable pageable) {
        QRamenReview review = QRamenReview.ramenReview;
        QMenu menu = QMenu.menu;
        QCategory category = QCategory.category;
        QRamenSoup soup = QRamenSoup.ramenSoup;

        BooleanBuilder builder = new BooleanBuilder();
        builder.and(board.postStatus.eq(CommunityActiveStatus.POSTED));
        builder.and(board.communityType.eq("review"));

        // 태그 타입에 따라 필터링 조건 추가
        if ("category".equals(type)) {
            builder.and(category.categoryName.eq(tag));
        } else if ("soup".equals(type)) {
            builder.and(soup.soupName.eq(tag));
        }

        List<BoardRespDto> results = queryFactory
                .select(getBoardRespDtoProjection())
                .from(board)
                .leftJoin(board.user, user)
                .join(review).on(review.communityId.eq(board.id))
                .join(review.menu, menu)
                .join(menu.category, category)
                .join(menu.ramenSoup, soup)
                .where(builder)
                .orderBy(board.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(board.count())
                .from(board)
                .join(review).on(review.communityId.eq(board.id))
                .join(review.menu, menu)
                .join(menu.category, category)
                .join(menu.ramenSoup, soup)
                .where(builder)
                .fetchOne();

        long totalCount = Optional.ofNullable(total).orElse(0L);

        return new PageImpl<>(results, pageable, totalCount);
    }
    
}
