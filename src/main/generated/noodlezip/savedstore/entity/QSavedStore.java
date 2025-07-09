package noodlezip.savedstore.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSavedStore is a Querydsl query type for SavedStore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSavedStore extends EntityPathBase<SavedStore> {

    private static final long serialVersionUID = -1531897235L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSavedStore savedStore = new QSavedStore("savedStore");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QSavedStoreLocation location;

    public final StringPath memo = createString("memo");

    public final QSavedStoreCategory saveStoreCategory;

    public final noodlezip.store.entity.QStore store;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final noodlezip.user.entity.QUser user;

    public QSavedStore(String variable) {
        this(SavedStore.class, forVariable(variable), INITS);
    }

    public QSavedStore(Path<? extends SavedStore> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSavedStore(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSavedStore(PathMetadata metadata, PathInits inits) {
        this(SavedStore.class, metadata, inits);
    }

    public QSavedStore(Class<? extends SavedStore> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.location = inits.isInitialized("location") ? new QSavedStoreLocation(forProperty("location")) : null;
        this.saveStoreCategory = inits.isInitialized("saveStoreCategory") ? new QSavedStoreCategory(forProperty("saveStoreCategory"), inits.get("saveStoreCategory")) : null;
        this.store = inits.isInitialized("store") ? new noodlezip.store.entity.QStore(forProperty("store")) : null;
        this.user = inits.isInitialized("user") ? new noodlezip.user.entity.QUser(forProperty("user")) : null;
    }

}

