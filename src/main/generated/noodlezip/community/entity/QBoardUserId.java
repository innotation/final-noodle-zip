package noodlezip.community.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoardUserId is a Querydsl query type for BoardUserId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBoardUserId extends BeanPath<BoardUserId> {

    private static final long serialVersionUID = -1189583806L;

    public static final QBoardUserId boardUserId = new QBoardUserId("boardUserId");

    public final StringPath communityId = createString("communityId");

    public final StringPath userId = createString("userId");

    public QBoardUserId(String variable) {
        super(BoardUserId.class, forVariable(variable));
    }

    public QBoardUserId(Path<? extends BoardUserId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoardUserId(PathMetadata metadata) {
        super(BoardUserId.class, metadata);
    }

}

