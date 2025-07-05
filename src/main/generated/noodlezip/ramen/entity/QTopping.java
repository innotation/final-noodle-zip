package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTopping is a Querydsl query type for Topping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTopping extends EntityPathBase<Topping> {

    private static final long serialVersionUID = -145486945L;

    public static final QTopping topping = new QTopping("topping");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isActive = createBoolean("isActive");

    public final StringPath toppingName = createString("toppingName");

    public QTopping(String variable) {
        super(Topping.class, forVariable(variable));
    }

    public QTopping(Path<? extends Topping> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTopping(PathMetadata metadata) {
        super(Topping.class, metadata);
    }

}

