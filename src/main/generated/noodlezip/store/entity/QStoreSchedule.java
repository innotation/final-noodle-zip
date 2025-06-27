package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStoreSchedule is a Querydsl query type for StoreSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreSchedule extends EntityPathBase<StoreSchedule> {

    private static final long serialVersionUID = -460228458L;

    public static final QStoreSchedule storeSchedule = new QStoreSchedule("storeSchedule");

    public final DateTimePath<java.time.LocalDateTime> closingAt = createDateTime("closingAt", java.time.LocalDateTime.class);

    public final StringPath dayOfWeek = createString("dayOfWeek");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isClosedDay = createBoolean("isClosedDay");

    public final DateTimePath<java.time.LocalDateTime> openingAt = createDateTime("openingAt", java.time.LocalDateTime.class);

    public final NumberPath<Long> storeId = createNumber("storeId", Long.class);

    public QStoreSchedule(String variable) {
        super(StoreSchedule.class, forVariable(variable));
    }

    public QStoreSchedule(Path<? extends StoreSchedule> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStoreSchedule(PathMetadata metadata) {
        super(StoreSchedule.class, metadata);
    }

}

