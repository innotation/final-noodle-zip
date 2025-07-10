package noodlezip.savedstore.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSavedStoreLocation is a Querydsl query type for SavedStoreLocation
 */
@Generated("com.querydsl.codegen.DefaultEmbeddableSerializer")
public class QSavedStoreLocation extends BeanPath<SavedStoreLocation> {

    private static final long serialVersionUID = -314285758L;

    public static final QSavedStoreLocation savedStoreLocation = new QSavedStoreLocation("savedStoreLocation");

    public final NumberPath<Double> storeLat = createNumber("storeLat", Double.class);

    public final NumberPath<Double> storeLng = createNumber("storeLng", Double.class);

    public QSavedStoreLocation(String variable) {
        super(SavedStoreLocation.class, forVariable(variable));
    }

    public QSavedStoreLocation(Path<? extends SavedStoreLocation> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSavedStoreLocation(PathMetadata metadata) {
        super(SavedStoreLocation.class, metadata);
    }

}

