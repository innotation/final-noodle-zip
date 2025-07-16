package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;
import noodlezip.store.status.ApprovalStatus;
import noodlezip.store.status.OperationStatus;
import noodlezip.store.status.ParkingType;

import java.util.ArrayList;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_store")
public class Store extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "store_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Size(max = 100)
    @NotNull
    @Column(name = "store_name", nullable = false, length = 100)
    private String storeName;

    @Size(max = 255)
    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @Size(max = 20)
    @Column(name = "phone", length = 20)
    private String phone;

    @NotNull
    @Column(name = "is_local_card", nullable = false)
    private Boolean isLocalCard;

    @NotNull
    @Column(name = "is_child_allowed", nullable = false)
    private Boolean isChildAllowed;

    // Enum 타입 3개 parking, operationstatus, approvalstatus
    @Enumerated(EnumType.STRING)
    @Column(name = "has_parking", nullable = false, length = 30)
    private ParkingType hasParking;

    @Enumerated(EnumType.STRING)
    @Column(name = "operation_status", nullable = false, length = 30)
    private OperationStatus operationStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "approval_status", nullable = false, length = 30)
    private ApprovalStatus approvalStatus;

    @Size(max = 300)
    @Column(name = "owner_comment", length = 300)
    private String ownerComment;

    @Size(max = 500)
    @Column(name = "store_main_image_url", length = 500)
    private String storeMainImageUrl;

    @NotNull
    @Column(name = "store_lat", nullable = false)
    private Double storeLat;

    @NotNull
    @Column(name = "store_lng", nullable = false)
    private Double storeLng;

    // 법정코드
    @NotNull
    @Column(name = "store_legal_code", nullable = false)
    private Integer storeLegalCode;

    // 사업자등록번호
    @NotNull
    @Column(name = "biz_num", nullable = false)
    private Long bizNum;
}
