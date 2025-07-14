package noodlezip.subscription.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUserSubscription is a Querydsl query type for UserSubscription
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserSubscription extends EntityPathBase<UserSubscription> {

    private static final long serialVersionUID = -1875221378L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QUserSubscription userSubscription = new QUserSubscription("userSubscription");

    public final DateTimePath<java.time.LocalDateTime> createdAt = createDateTime("createdAt", java.time.LocalDateTime.class);

    public final noodlezip.user.entity.QUser followee;

    public final noodlezip.user.entity.QUser follower;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QUserSubscription(String variable) {
        this(UserSubscription.class, forVariable(variable), INITS);
    }

    public QUserSubscription(Path<? extends UserSubscription> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QUserSubscription(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QUserSubscription(PathMetadata metadata, PathInits inits) {
        this(UserSubscription.class, metadata, inits);
    }

    public QUserSubscription(Class<? extends UserSubscription> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.followee = inits.isInitialized("followee") ? new noodlezip.user.entity.QUser(forProperty("followee")) : null;
        this.follower = inits.isInitialized("follower") ? new noodlezip.user.entity.QUser(forProperty("follower")) : null;
    }

}

