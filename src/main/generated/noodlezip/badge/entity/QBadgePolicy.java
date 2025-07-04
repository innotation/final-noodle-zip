package noodlezip.badge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QBadgePolicy is a Querydsl query type for BadgePolicy
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBadgePolicy extends BeanPath<BadgePolicy> {

    private static final long serialVersionUID = -697525711L;

    public static final QBadgePolicy badgePolicy = new QBadgePolicy("badgePolicy");

    public final NumberPath<Integer> badgeLevel = createNumber("badgeLevel", Integer.class);

    public final NumberPath<Integer> completionValue = createNumber("completionValue", Integer.class);

    public final NumberPath<Long> nextBadgeId = createNumber("nextBadgeId", Long.class);

    public QBadgePolicy(String variable) {
        super(BadgePolicy.class, forVariable(variable));
    }

    public QBadgePolicy(Path<? extends BadgePolicy> path) {
        super(path.getType(), path.getMetadata());
    }

    public QBadgePolicy(PathMetadata metadata) {
        super(BadgePolicy.class, metadata);
    }

}

