package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStoreWeekSchedule is a Querydsl query type for StoreWeekSchedule
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStoreWeekSchedule extends EntityPathBase<StoreWeekSchedule> {

    private static final long serialVersionUID = 400372010L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStoreWeekSchedule storeWeekSchedule = new QStoreWeekSchedule("storeWeekSchedule");

    public final TimePath<java.time.LocalTime> closingAt = createTime("closingAt", java.time.LocalTime.class);

    public final QStoreWeekScheduleId id;

    public final BooleanPath isClosedDay = createBoolean("isClosedDay");

    public final TimePath<java.time.LocalTime> openingAt = createTime("openingAt", java.time.LocalTime.class);

    public final QStore storeId;

    public QStoreWeekSchedule(String variable) {
        this(StoreWeekSchedule.class, forVariable(variable), INITS);
    }

    public QStoreWeekSchedule(Path<? extends StoreWeekSchedule> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStoreWeekSchedule(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStoreWeekSchedule(PathMetadata metadata, PathInits inits) {
        this(StoreWeekSchedule.class, metadata, inits);
    }

    public QStoreWeekSchedule(Class<? extends StoreWeekSchedule> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.id = inits.isInitialized("id") ? new QStoreWeekScheduleId(forProperty("id")) : null;
        this.storeId = inits.isInitialized("storeId") ? new QStore(forProperty("storeId")) : null;
    }

}

