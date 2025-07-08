package noodlezip.community.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.entity.QBoard;
import noodlezip.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public BoardRespDto findBoardByBoardIdWithUser(Long boardId) {
        QBoard board = QBoard.board;
        QUser user = QUser.user;

        BoardRespDto result = queryFactory
                .select(Projections.fields(BoardRespDto.class,
                        board.id.as("boardId"),
                        board.user.id.as("userId"),
                        board.user.userName.as("userName"),
                        board.title.as("title"),
                        board.content.as("content"),
                        board.communityType.as("communityType"),
                        board.postStatus.as("postStatus"),
                        board.likesCount.as("likesCount"),
                        board.viewsCount.as("viewsCount"),
                        board.createdAt.as("createdAt"),
                        board.updatedAt.as("updatedAt"),
                        board.imageUrl.as("imageUrl")
                ))
                .from(board)
                .leftJoin(board.user, user)
                .where(
                        board.id.eq(boardId).and(board.postStatus.eq(CommunityActiveStatus.POSTED))
                )
                .fetchOne();
        return result;
    }

    @Override
    public Page<BoardRespDto> findBoardWithPagination(Pageable pageable) {
        QBoard board = QBoard.board;
        QUser user = QUser.user;

        List<BoardRespDto> results = queryFactory
                .select(Projections.fields(BoardRespDto.class,
                        board.id.as("boardId"),
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
                ))
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

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Page<BoardRespDto> findBoardWithPaginationAndCommunityType(String category,Pageable pageable) {
        QBoard board = QBoard.board;
        QUser user = QUser.user;


        List<BoardRespDto> results = queryFactory
                .select(Projections.fields(BoardRespDto.class,
                        board.id.as("boardId"),
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
                ))
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

        return new PageImpl<>(results, pageable, total);
    }

    @Override
    public Long increaseViewCount(Long boardId, Long viewCount) {
        QBoard board = QBoard.board;

        Long result = queryFactory
                .update(board)
                .set(board.viewsCount, board.viewsCount.add(viewCount))
                .where(board.id.eq(boardId))
                .execute();
        return result;
    }
}
