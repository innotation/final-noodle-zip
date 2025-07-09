package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QReviewTopping is a Querydsl query type for ReviewTopping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QReviewTopping extends EntityPathBase<ReviewTopping> {

    private static final long serialVersionUID = -1177725145L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QReviewTopping reviewTopping = new QReviewTopping("reviewTopping");

    public final QReviewToppingId id;

    public final QRamenReview ramenReview;

    public final noodlezip.store.entity.QStoreExtraTopping storeExtraTopping;

    public QReviewTopping(String variable) {
        this(ReviewTopping.class, forVariable(variable), INITS);
    }

    public QReviewTopping(Path<? extends ReviewTopping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QReviewTopping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QReviewTopping(PathMetadata metadata, PathInits inits) {
        this(ReviewTopping.class, metadata, inits);
    }

    public QReviewTopping(Class<? extends ReviewTopping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QReviewToppingId(forProperty("id")) : null;
        this.ramenReview = inits.isInitialized("ramenReview") ? new QRamenReview(forProperty("ramenReview"), inits.get("ramenReview")) : null;
        this.storeExtraTopping = inits.isInitialized("storeExtraTopping") ? new noodlezip.store.entity.QStoreExtraTopping(forProperty("storeExtraTopping"), inits.get("storeExtraTopping")) : null;
    }

}

