package noodlezip.badge.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBadgeExtraOption is a Querydsl query type for BadgeExtraOption
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QBadgeExtraOption extends BeanPath<BadgeExtraOption> {

    private static final long serialVersionUID = 968429318L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBadgeExtraOption badgeExtraOption = new QBadgeExtraOption("badgeExtraOption");

    public final noodlezip.ramen.entity.QCategory ramenCategory;

    public final NumberPath<Integer> storeSidoLegalCode = createNumber("storeSidoLegalCode", Integer.class);

    public QBadgeExtraOption(String variable) {
        this(BadgeExtraOption.class, forVariable(variable), INITS);
    }

    public QBadgeExtraOption(Path<? extends BadgeExtraOption> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBadgeExtraOption(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBadgeExtraOption(PathMetadata metadata, PathInits inits) {
        this(BadgeExtraOption.class, metadata, inits);
    }

    public QBadgeExtraOption(Class<? extends BadgeExtraOption> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.ramenCategory = inits.isInitialized("ramenCategory") ? new noodlezip.ramen.entity.QCategory(forProperty("ramenCategory")) : null;
    }

}

