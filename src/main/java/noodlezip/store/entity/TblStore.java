package noodlezip.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_store")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TblStore {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id")
    private Long id;

    @Column(nullable = false, length = 100)
    private String storeName;

    @Column(nullable = false, length = 300)
    private String address;

    @Column(nullable = false, length = 20)
    private String phone;

    private Boolean isLocalCard;
    private Boolean isChildAllowed;

    @Column(length = 10)
    private String hasParking;

    @Column(length = 300)
    private String ownerComment;

    @Column(length = 500)
    private String storeMainImageUrl;

    private Double xAxis;
    private Double yAxis;
}
