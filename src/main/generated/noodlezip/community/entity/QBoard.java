package noodlezip.community.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -661922084L;

    public static final QBoard board = new QBoard("board");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    public final StringPath communityType = createString("communityType");

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath imageUrl = createString("imageUrl");

    public final NumberPath<Integer> likesCount = createNumber("likesCount", Integer.class);

    public final EnumPath<CommunityActiveStatus> postStatus = createEnum("postStatus", CommunityActiveStatus.class);

    public final NumberPath<Long> reviewStoreId = createNumber("reviewStoreId", Long.class);

    public final DateTimePath<java.time.Instant> reviewVisitDate = createDateTime("reviewVisitDate", java.time.Instant.class);

    public final StringPath title = createString("title");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public final NumberPath<Integer> viewsCount = createNumber("viewsCount", Integer.class);

    public QBoard(String variable) {
        super(Board.class, forVariable(variable));
    }

    public QBoard(Path<? extends Board> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBoard(PathMetadata metadata) {
        super(Board.class, metadata);
    }

}

