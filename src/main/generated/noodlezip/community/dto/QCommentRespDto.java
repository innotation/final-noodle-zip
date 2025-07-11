package noodlezip.community.dto;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.ConstructorExpression;
import javax.annotation.processing.Generated;

/**
 * noodlezip.community.dto.QCommentRespDto is a Querydsl Projection type for CommentRespDto
 */
@Generated("com.querydsl.codegen.DefaultProjectionSerializer")
public class QCommentRespDto extends ConstructorExpression<CommentRespDto> {

    private static final long serialVersionUID = 1160121792L;

    public QCommentRespDto(com.querydsl.core.types.Expression<Long> id, com.querydsl.core.types.Expression<String> author, com.querydsl.core.types.Expression<String> authorProfileImageUrl, com.querydsl.core.types.Expression<Long> userId, com.querydsl.core.types.Expression<String> content, com.querydsl.core.types.Expression<java.time.LocalDateTime> createdAt, com.querydsl.core.types.Expression<java.time.LocalDateTime> updatedAt) {
        super(CommentRespDto.class, new Class<?>[]{long.class, String.class, String.class, long.class, String.class, java.time.LocalDateTime.class, java.time.LocalDateTime.class}, id, author, authorProfileImageUrl, userId, content, createdAt, updatedAt);
    }

}

