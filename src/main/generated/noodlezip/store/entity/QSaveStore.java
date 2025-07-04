package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSaveStore is a Querydsl query type for SaveStore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSaveStore extends EntityPathBase<SaveStore> {

    private static final long serialVersionUID = 654399074L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSaveStore saveStore = new QSaveStore("saveStore");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath memo = createString("memo");

    public final QSaveStoreCategory saveStoreCategoryId;

    public final QStore storeId;

    public final NumberPath<Double> storeLat = createNumber("storeLat", Double.class);

    public final NumberPath<Double> storeLng = createNumber("storeLng", Double.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final noodlezip.user.entity.QUser userId;

    public QSaveStore(String variable) {
        this(SaveStore.class, forVariable(variable), INITS);
    }

    public QSaveStore(Path<? extends SaveStore> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSaveStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSaveStore(PathMetadata metadata, PathInits inits) {
        this(SaveStore.class, metadata, inits);
    }

    public QSaveStore(Class<? extends SaveStore> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.saveStoreCategoryId = inits.isInitialized("saveStoreCategoryId") ? new QSaveStoreCategory(forProperty("saveStoreCategoryId")) : null;
        this.storeId = inits.isInitialized("storeId") ? new QStore(forProperty("storeId")) : null;
        this.userId = inits.isInitialized("userId") ? new noodlezip.user.entity.QUser(forProperty("userId")) : null;
    }

}

