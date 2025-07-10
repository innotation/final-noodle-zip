package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QRamenReview is a Querydsl query type for RamenReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRamenReview extends EntityPathBase<RamenReview> {

    private static final long serialVersionUID = -257829161L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QRamenReview ramenReview = new QRamenReview("ramenReview");

    public final NumberPath<Long> communityId = createNumber("communityId", Long.class);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isReceiptReview = createBoolean("isReceiptReview");

    public final noodlezip.store.entity.QMenu menu;

    public final NumberPath<Integer> noodleBoilLevel = createNumber("noodleBoilLevel", Integer.class);

    public final NumberPath<Integer> noodleTexture = createNumber("noodleTexture", Integer.class);

    public final NumberPath<Integer> noodleThickness = createNumber("noodleThickness", Integer.class);

    public final StringPath ocrKeyHash = createString("ocrKeyHash");

    public final StringPath reviewImageUrl = createString("reviewImageUrl");

    public final NumberPath<Integer> soupDensity = createNumber("soupDensity", Integer.class);

    public final StringPath soupFlavorKeywords = createString("soupFlavorKeywords");

    public final NumberPath<Integer> soupOiliness = createNumber("soupOiliness", Integer.class);

    public final NumberPath<Integer> soupSaltiness = createNumber("soupSaltiness", Integer.class);

    public final NumberPath<Integer> soupSpicinessLevel = createNumber("soupSpicinessLevel", Integer.class);

    public final NumberPath<Integer> soupTemperature = createNumber("soupTemperature", Integer.class);

    public QRamenReview(String variable) {
        this(RamenReview.class, forVariable(variable), INITS);
    }

    public QRamenReview(Path<? extends RamenReview> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QRamenReview(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QRamenReview(PathMetadata metadata, PathInits inits) {
        this(RamenReview.class, metadata, inits);
    }

    public QRamenReview(Class<? extends RamenReview> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.menu = inits.isInitialized("menu") ? new noodlezip.store.entity.QMenu(forProperty("menu"), inits.get("menu")) : null;
    }

}

