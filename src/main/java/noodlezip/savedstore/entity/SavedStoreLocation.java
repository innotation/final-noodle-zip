package noodlezip.savedstore.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Embeddable
public class SavedStoreLocation {

    @Column(name = "store_lat")
    private Double storeLat;

    @Column(name = "store_lng")
    private Double storeLng;

}
