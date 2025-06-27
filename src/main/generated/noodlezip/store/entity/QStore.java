package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStore is a Querydsl query type for Store
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStore extends EntityPathBase<Store> {

    private static final long serialVersionUID = 2072236255L;

    public static final QStore store = new QStore("store");

    public final StringPath address = createString("address");

    public final StringPath hasParking = createString("hasParking");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChildAllowed = createBoolean("isChildAllowed");

    public final BooleanPath isLocalCard = createBoolean("isLocalCard");

    public final StringPath ownerComment = createString("ownerComment");

    public final StringPath phone = createString("phone");

    public final StringPath storeMainImageUrl = createString("storeMainImageUrl");

    public final StringPath storeName = createString("storeName");

    public final NumberPath<Double> xAxis = createNumber("xAxis", Double.class);

    public final NumberPath<Double> yAxis = createNumber("yAxis", Double.class);

    public QStore(String variable) {
        super(Store.class, forVariable(variable));
    }

    public QStore(Path<? extends Store> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStore(PathMetadata metadata) {
        super(Store.class, metadata);
    }

}

