package noodlezip.badge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBadgeGroup is a Querydsl query type for BadgeGroup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBadgeGroup extends EntityPathBase<BadgeGroup> {

    private static final long serialVersionUID = 1354753440L;

    public static final QBadgeGroup badgeGroup = new QBadgeGroup("badgeGroup");

    public final ListPath<BadgeCategory, QBadgeCategory> badgeCategoryList = this.<BadgeCategory, QBadgeCategory>createList("badgeCategoryList", BadgeCategory.class, QBadgeCategory.class, PathInits.DIRECT2);

    public final StringPath badgeGroupName = createString("badgeGroupName");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBadgeGroup(String variable) {
        super(BadgeGroup.class, forVariable(variable));
    }

    public QBadgeGroup(Path<? extends BadgeGroup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBadgeGroup(PathMetadata metadata) {
        super(BadgeGroup.class, metadata);
    }

}

