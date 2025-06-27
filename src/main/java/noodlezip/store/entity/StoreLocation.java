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
    private Long storeId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "store_id")
    private Store store;

    @NotNull
    @Column(name = "x_axis", nullable = false)
    private Double xAxis;

    @NotNull
    @Column(name = "y_axis", nullable = false)
    private Double yAxis;

}