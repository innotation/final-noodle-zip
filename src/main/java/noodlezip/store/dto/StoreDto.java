package noodlezip.store.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.store.status.ApprovalStatus;
import noodlezip.store.status.OperationStatus;
import noodlezip.store.status.ParkingType;
import noodlezip.store.entity.Store;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreDto implements Serializable {
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    @Size(max = 100)
    private String storeName;

    @NotNull
    @Size(max = 255)
    private String address;

    @Size(max = 20)
    private String phone;

    @NotNull
    private Boolean isLocalCard;

    @NotNull
    private Boolean isChildAllowed;

    // Enum
    @NotNull
    private ParkingType hasParking;

    @NotNull
    private OperationStatus operationStatus;

    @NotNull
    private ApprovalStatus approvalStatus;

    @Size(max = 300)
    private String ownerComment;

    @Size(max = 500)
    private String storeMainImageUrl;

    @NotNull
    private Double storeLat;

    @NotNull
    private Double storeLng;

    @NotNull
    private Integer storeLegalCode; // 법정코드
    
}