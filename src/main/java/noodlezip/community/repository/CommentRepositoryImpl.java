package noodlezip.community.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import noodlezip.community.dto.CommentRespDto;
import noodlezip.community.dto.QCommentRespDto;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.entity.QComment;
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

        // 엔티티 반환 구조, 대댓글을 통한 서비스 확장시 등
        // 댓글 엔티티 자체가 필요할 경우 유용함
//        List<Comment> commentList = queryFactory
//                .selectFrom(comment)
//                .leftJoin(comment.user, user).fetchJoin()
//                .where(
//                        comment.board.id.eq(boardId)
//                )
//                .orderBy(comment.createdAt.desc())
//                .offset(pageable.getOffset())
//                .limit(pageable.getPageSize())
//                .fetch();

        // Comment Dto로 프로젝션 하는 방식, 현재의 댓글이 level1만 있는 경우 유용
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
}
