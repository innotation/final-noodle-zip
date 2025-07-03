package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_store_location")
public class StoreLocation {

    @Id
    @Column(name = "store_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "store_id")
    private Store storeId;

    @Column(name = "store_lat", nullable = false)
    private Double storeLat;

    @Column(name = "store_lng", nullable = false)
    private Double storeLng;
}