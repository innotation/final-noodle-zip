package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QReviewToppingId is a Querydsl query type for ReviewToppingId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QReviewToppingId extends BeanPath<ReviewToppingId> {

    private static final long serialVersionUID = 2077504162L;

    public static final QReviewToppingId reviewToppingId = new QReviewToppingId("reviewToppingId");

    public final NumberPath<Long> ramenReviewId = createNumber("ramenReviewId", Long.class);

    public final NumberPath<Long> storeExtraToppingId = createNumber("storeExtraToppingId", Long.class);

    public QReviewToppingId(String variable) {
        super(ReviewToppingId.class, forVariable(variable));
    }

    public QReviewToppingId(Path<? extends ReviewToppingId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QReviewToppingId(PathMetadata metadata) {
        super(ReviewToppingId.class, metadata);
    }

}

