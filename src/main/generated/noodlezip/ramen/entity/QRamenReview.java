package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRamenReview is a Querydsl query type for RamenReview
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRamenReview extends EntityPathBase<RamenReview> {

    private static final long serialVersionUID = -257829161L;

    public static final QRamenReview ramenReview = new QRamenReview("ramenReview");

    public final NumberPath<Long> communityId = createNumber("communityId", Long.class);

    public final StringPath content = createString("content");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isReceiptReview = createBoolean("isReceiptReview");

    public final NumberPath<Long> menuId = createNumber("menuId", Long.class);

    public final StringPath noodleBoilLevel = createString("noodleBoilLevel");

    public final StringPath noodleTexture = createString("noodleTexture");

    public final StringPath noodleThickness = createString("noodleThickness");

    public final StringPath reviewImageUrl = createString("reviewImageUrl");

    public final StringPath soupDensity = createString("soupDensity");

    public final StringPath soupFlavorKeywords = createString("soupFlavorKeywords");

    public final StringPath soupOiliness = createString("soupOiliness");

    public final StringPath soupSaltiness = createString("soupSaltiness");

    public final StringPath soupSpicinessLevel = createString("soupSpicinessLevel");

    public final StringPath soupTemperature = createString("soupTemperature");

    public QRamenReview(String variable) {
        super(RamenReview.class, forVariable(variable));
    }

    public QRamenReview(Path<? extends RamenReview> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRamenReview(PathMetadata metadata) {
        super(RamenReview.class, metadata);
    }

}

