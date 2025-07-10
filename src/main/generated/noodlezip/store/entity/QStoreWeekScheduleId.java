package noodlezip.store.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QStoreWeekScheduleId is a Querydsl query type for StoreWeekScheduleId
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QStoreWeekScheduleId extends BeanPath<StoreWeekScheduleId> {

    private static final long serialVersionUID = -1789552667L;

    public static final QStoreWeekScheduleId storeWeekScheduleId = new QStoreWeekScheduleId("storeWeekScheduleId");

    public final StringPath dayOfWeek = createString("dayOfWeek");

    public final NumberPath<Long> storeId = createNumber("storeId", Long.class);

    public QStoreWeekScheduleId(String variable) {
        super(StoreWeekScheduleId.class, forVariable(variable));
    }

    public QStoreWeekScheduleId(Path<? extends StoreWeekScheduleId> path) {
        super(path.getType(), path.getMetadata());
    }

    public QStoreWeekScheduleId(PathMetadata metadata) {
        super(StoreWeekScheduleId.class, metadata);
    }

}

