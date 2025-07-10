package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreExtraTopping is a Querydsl query type for StoreExtraTopping
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreExtraTopping extends EntityPathBase<StoreExtraTopping> {

    private static final long serialVersionUID = -1712672618L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreExtraTopping storeExtraTopping = new QStoreExtraTopping("storeExtraTopping");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> price = createNumber("price", Integer.class);

    public final QStore store;

    public final noodlezip.ramen.entity.QTopping topping;

    public QStoreExtraTopping(String variable) {
        this(StoreExtraTopping.class, forVariable(variable), INITS);
    }

    public QStoreExtraTopping(Path<? extends StoreExtraTopping> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreExtraTopping(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreExtraTopping(PathMetadata metadata, PathInits inits) {
        this(StoreExtraTopping.class, metadata, inits);
    }

    public QStoreExtraTopping(Class<? extends StoreExtraTopping> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.store = inits.isInitialized("store") ? new QStore(forProperty("store")) : null;
        this.topping = inits.isInitialized("topping") ? new noodlezip.ramen.entity.QTopping(forProperty("topping")) : null;
    }

}

