package noodlezip.badge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserBadge is a Querydsl query type for UserBadge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserBadge extends EntityPathBase<UserBadge> {

    private static final long serialVersionUID = -2033579756L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserBadge userBadge = new QUserBadge("userBadge");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    public final NumberPath<Integer> accumulativeValue = createNumber("accumulativeValue", Integer.class);

    public final QBadge badge;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> currentValue = createNumber("currentValue", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final DateTimePath<java.time.LocalDateTime> obtainedAt = createDateTime("obtainedAt", java.time.LocalDateTime.class);

    public final EnumPath<noodlezip.badge.constants.PostStatusType> postStatus = createEnum("postStatus", noodlezip.badge.constants.PostStatusType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

    public QUserBadge(String variable) {
        this(UserBadge.class, forVariable(variable), INITS);
    }

    public QUserBadge(Path<? extends UserBadge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserBadge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserBadge(PathMetadata metadata, PathInits inits) {
        this(UserBadge.class, metadata, inits);
    }

    public QUserBadge(Class<? extends UserBadge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.badge = inits.isInitialized("badge") ? new QBadge(forProperty("badge"), inits.get("badge")) : null;
    }

}

