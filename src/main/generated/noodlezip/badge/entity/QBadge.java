package noodlezip.badge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBadge is a Querydsl query type for Badge
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBadge extends EntityPathBase<Badge> {

    private static final long serialVersionUID = -494019553L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBadge badge = new QBadge("badge");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    public final QBadgeCategory badgeCategory;

    public final QBadgeExtraOption badgeExtraOption;

    public final StringPath badgeImageUrl = createString("badgeImageUrl");

    public final StringPath badgeName = createString("badgeName");

    public final QBadgePolicy badgePolicy;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QBadge(String variable) {
        this(Badge.class, forVariable(variable), INITS);
    }

    public QBadge(Path<? extends Badge> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBadge(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBadge(PathMetadata metadata, PathInits inits) {
        this(Badge.class, metadata, inits);
    }

    public QBadge(Class<? extends Badge> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.badgeCategory = inits.isInitialized("badgeCategory") ? new QBadgeCategory(forProperty("badgeCategory"), inits.get("badgeCategory")) : null;
        this.badgeExtraOption = inits.isInitialized("badgeExtraOption") ? new QBadgeExtraOption(forProperty("badgeExtraOption"), inits.get("badgeExtraOption")) : null;
        this.badgePolicy = inits.isInitialized("badgePolicy") ? new QBadgePolicy(forProperty("badgePolicy")) : null;
    }

}

