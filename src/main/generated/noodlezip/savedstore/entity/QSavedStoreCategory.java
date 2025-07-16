package noodlezip.savedstore.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QSavedStoreCategory is a Querydsl query type for SavedStoreCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSavedStoreCategory extends EntityPathBase<SavedStoreCategory> {

    private static final long serialVersionUID = 2130149003L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QSavedStoreCategory savedStoreCategory = new QSavedStoreCategory("savedStoreCategory");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    public final StringPath categoryName = createString("categoryName");

    public final NumberPath<Integer> categoryOrder = createNumber("categoryOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isPublic = createBoolean("isPublic");

    public final ListPath<SavedStore, QSavedStore> saveStoreList = this.<SavedStore, QSavedStore>createList("saveStoreList", SavedStore.class, QSavedStore.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final noodlezip.user.entity.QUser user;

    public QSavedStoreCategory(String variable) {
        this(SavedStoreCategory.class, forVariable(variable), INITS);
    }

    public QSavedStoreCategory(Path<? extends SavedStoreCategory> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QSavedStoreCategory(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QSavedStoreCategory(PathMetadata metadata, PathInits inits) {
        this(SavedStoreCategory.class, metadata, inits);
    }

    public QSavedStoreCategory(Class<? extends SavedStoreCategory> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new noodlezip.user.entity.QUser(forProperty("user")) : null;
    }

}

