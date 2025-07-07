package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSaveStoreCategory is a Querydsl query type for SaveStoreCategory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSaveStoreCategory extends EntityPathBase<SaveStoreCategory> {

    private static final long serialVersionUID = 1549464960L;

    public static final QSaveStoreCategory saveStoreCategory = new QSaveStoreCategory("saveStoreCategory");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    public final StringPath categoryId = createString("categoryId");

    public final NumberPath<Integer> categoryOrder = createNumber("categoryOrder", Integer.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath publicStatus = createBoolean("publicStatus");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QSaveStoreCategory(String variable) {
        super(SaveStoreCategory.class, forVariable(variable));
    }

    public QSaveStoreCategory(Path<? extends SaveStoreCategory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSaveStoreCategory(PathMetadata metadata) {
        super(SaveStoreCategory.class, metadata);
    }

}

