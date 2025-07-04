package noodlezip.badge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBadgeCategory is a Querydsl query type for BadgeCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBadgeCategory extends EntityPathBase<BadgeCategory> {

    private static final long serialVersionUID = -803705283L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBadgeCategory badgeCategory = new QBadgeCategory("badgeCategory");

    public final StringPath badgeCategoryName = createString("badgeCategoryName");

    public final StringPath badgeDescription = createString("badgeDescription");

    public final QBadgeGroup badgeGroup;

    public final ListPath<Badge, QBadge> badgeList = this.<Badge, QBadge>createList("badgeList", Badge.class, QBadge.class, PathInits.DIRECT2);

    public final EnumPath<noodlezip.badge.constants.BadgeStrategyType> badgeStrategy = createEnum("badgeStrategy", noodlezip.badge.constants.BadgeStrategyType.class);

    public final DatePath<java.time.LocalDate> eventEndAt = createDate("eventEndAt", java.time.LocalDate.class);

    public final DatePath<java.time.LocalDate> eventStartAt = createDate("eventStartAt", java.time.LocalDate.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public QBadgeCategory(String variable) {
        this(BadgeCategory.class, forVariable(variable), INITS);
    }

    public QBadgeCategory(Path<? extends BadgeCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBadgeCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBadgeCategory(PathMetadata metadata, PathInits inits) {
        this(BadgeCategory.class, metadata, inits);
    }

    public QBadgeCategory(Class<? extends BadgeCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.badgeGroup = inits.isInitialized("badgeGroup") ? new QBadgeGroup(forProperty("badgeGroup")) : null;
    }

}

