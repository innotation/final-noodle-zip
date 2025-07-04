package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRamenTopping is a Querydsl query type for RamenTopping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRamenTopping extends EntityPathBase<RamenTopping> {

    private static final long serialVersionUID = -1641766744L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRamenTopping ramenTopping = new QRamenTopping("ramenTopping");

    public final QRamenToppingId id;

    public final noodlezip.store.entity.QMenu menu;

    public final QTopping topping;

    public QRamenTopping(String variable) {
        this(RamenTopping.class, forVariable(variable), INITS);
    }

    public QRamenTopping(Path<? extends RamenTopping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRamenTopping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRamenTopping(PathMetadata metadata, PathInits inits) {
        this(RamenTopping.class, metadata, inits);
    }

    public QRamenTopping(Class<? extends RamenTopping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QRamenToppingId(forProperty("id")) : null;
        this.menu = inits.isInitialized("menu") ? new noodlezip.store.entity.QMenu(forProperty("menu"), inits.get("menu")) : null;
        this.topping = inits.isInitialized("topping") ? new QTopping(forProperty("topping")) : null;
    }

}

