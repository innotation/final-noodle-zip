package noodlezip.community.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.community.dto.CommentRespDto;
import noodlezip.community.dto.QCommentRespDto;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.entity.QBoard;
import noodlezip.community.entity.QComment;
import noodlezip.mypage.dto.response.MyCommentResponse;
import noodlezip.user.entity.QUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<CommentRespDto> findCommentByBoardIdWithUser(Long boardId, Long userId,Pageable pageable) {
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        List<CommentRespDto> results = queryFactory
                .select(new QCommentRespDto(
                        comment.id,
                        comment.user.userName,
                        comment.user.profileImageUrl,
                        comment.user.id,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt
                        ))
                .from(comment)
                .leftJoin(comment.user, user)
                .where(
                        comment.communityId.eq(boardId)
                )
                .orderBy(comment.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.communityId.eq(boardId).and(comment.commentStatus.eq(CommunityActiveStatus.POSTED))
                )
                .fetchOne();
        List<CommentRespDto> content = results.stream()
                .peek(dto -> {
                    dto.setWriter(userId != null && dto.getUserId().equals(userId));
                })
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<MyCommentResponse> findCommentByUserId(Long userId, Pageable pageable) {
        QComment comment = QComment.comment;
        QBoard board = QBoard.board;
        QUser user = QUser.user;

        List<MyCommentResponse> results = queryFactory
                .select(Projections.constructor(MyCommentResponse.class,
                        comment.id,
                        comment.communityId,
                        board.title,
                        comment.user.userName,
                        comment.user.profileImageUrl,
                        comment.user.id,
                        comment.content,
                        comment.createdAt,
                        comment.updatedAt
                ))
                .from(comment)
                .leftJoin(comment.user, user)
                .leftJoin(board).on(comment.communityId.eq(board.id))
                .where(
                        comment.user.id.eq(userId)
                )
                .orderBy(comment.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(comment.count())
                .from(comment)
                .where(
                        comment.user.id.eq(userId)
                )
                .fetchOne();

        return new PageImpl<>(results, pageable, total == null ? 0 : total);
    }
}
