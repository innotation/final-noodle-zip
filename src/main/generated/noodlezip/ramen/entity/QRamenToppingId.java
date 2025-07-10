package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRamenToppingId is a Querydsl query type for RamenToppingId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QRamenToppingId extends BeanPath<RamenToppingId> {

    private static final long serialVersionUID = -1484840989L;

    public static final QRamenToppingId ramenToppingId = new QRamenToppingId("ramenToppingId");

    public final NumberPath<Long> menuId = createNumber("menuId", Long.class);

    public final NumberPath<Long> toppingId = createNumber("toppingId", Long.class);

    public QRamenToppingId(String variable) {
        super(RamenToppingId.class, forVariable(variable));
    }

    public QRamenToppingId(Path<? extends RamenToppingId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRamenToppingId(PathMetadata metadata) {
        super(RamenToppingId.class, metadata);
    }

}

