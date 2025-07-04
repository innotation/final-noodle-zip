package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreLocation is a Querydsl query type for StoreLocation
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreLocation extends EntityPathBase<StoreLocation> {

    private static final long serialVersionUID = 2138736052L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreLocation storeLocation = new QStoreLocation("storeLocation");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QStore storeId;

    public final NumberPath<Double> storeLat = createNumber("storeLat", Double.class);

    public final NumberPath<Double> storeLng = createNumber("storeLng", Double.class);

    public QStoreLocation(String variable) {
        this(StoreLocation.class, forVariable(variable), INITS);
    }

    public QStoreLocation(Path<? extends StoreLocation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreLocation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreLocation(PathMetadata metadata, PathInits inits) {
        this(StoreLocation.class, metadata, inits);
    }

    public QStoreLocation(Class<? extends StoreLocation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.storeId = inits.isInitialized("storeId") ? new QStore(forProperty("storeId")) : null;
    }

}

