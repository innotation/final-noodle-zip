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

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    public final StringPath address = createString("address");

    public final EnumPath<noodlezip.store.status.ApprovalStatus> approvalStatus = createEnum("approvalStatus", noodlezip.store.status.ApprovalStatus.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final EnumPath<noodlezip.store.status.ParkingType> hasParking = createEnum("hasParking", noodlezip.store.status.ParkingType.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isChildAllowed = createBoolean("isChildAllowed");

    public final BooleanPath isLocalCard = createBoolean("isLocalCard");

    public final EnumPath<noodlezip.store.status.OperationStatus> operationStatus = createEnum("operationStatus", noodlezip.store.status.OperationStatus.class);

    public final StringPath ownerComment = createString("ownerComment");

    public final StringPath phone = createString("phone");

    public final NumberPath<Double> storeLat = createNumber("storeLat", Double.class);

    public final NumberPath<Integer> storeLegalCode = createNumber("storeLegalCode", Integer.class);

    public final NumberPath<Double> storeLng = createNumber("storeLng", Double.class);

    public final StringPath storeMainImageUrl = createString("storeMainImageUrl");

    public final StringPath storeName = createString("storeName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> userId = createNumber("userId", Long.class);

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

