package noodlezip.store.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tbl_store")
public class Store {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long storeId;

    private Long userId;
    private String storeName;
    private String address;
    private String phone;

    private Boolean isLocalCard;
    private Boolean isChildAllowed;
    private String hasParking;
    private String operationStatus;
    private String ownerComment;
    private String storeMainImageUrl;
    private Double xAxis;
    private Double yAxis;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL)
    private List<StoreWeekSchedule> weekSchedules = new ArrayList<>();
}