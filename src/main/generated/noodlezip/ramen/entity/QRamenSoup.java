package noodlezip.ramen.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QRamenSoup is a Querydsl query type for RamenSoup
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QRamenSoup extends EntityPathBase<RamenSoup> {

    private static final long serialVersionUID = -362239690L;

    public static final QRamenSoup ramenSoup = new QRamenSoup("ramenSoup");

    public final noodlezip.common.entity.QBaseTimeEntity _super = new noodlezip.common.entity.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Integer> id = createNumber("id", Integer.class);

    public final StringPath soupName = createString("soupName");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QRamenSoup(String variable) {
        super(RamenSoup.class, forVariable(variable));
    }

    public QRamenSoup(Path<? extends RamenSoup> path) {
        super(path.getType(), path.getMetadata());
    }

    public QRamenSoup(PathMetadata metadata) {
        super(RamenSoup.class, metadata);
    }

}

