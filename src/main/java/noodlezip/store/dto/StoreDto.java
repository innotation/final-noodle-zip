package noodlezip.store.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import noodlezip.store.entity.Store;

import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDateTime;

/**
 * DTO for {@link noodlezip.store.entity.Store}
 */
@Value
@Builder
public class StoreDto implements Serializable {
    Long id;
    @NotNull
    Long userId;
    @NotNull
    @Size(max = 100)
    String storeName;
    @NotNull
    @Size(max = 255)
    String address;
    @Size(max = 20)
    String phone;
    @NotNull
    Boolean isLocalCard;
    @NotNull
    Boolean isChildAllowed;
    @NotNull
    @Size(max = 30)
    String hasParking;
    @NotNull
    @Size(max = 30)
    String operationStatus;
    @Size(max = 300)
    String ownerComment;
    @Size(max = 500)
    String storeMainImageUrl;
    @NotNull
    Double storeLat;
    @NotNull
    Double storeLng;
    @NotNull
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    Integer storeLegalCode;
    String approvalStatus;


    // Store -> StoreDto
    public static StoreDto toDto(Store store) {
        return new StoreDto(
                store.getId(),
                store.getUserId(),
                store.getStoreName(),
                store.getAddress(),
                store.getPhone(),
                store.getIsLocalCard(),
                store.getIsChildAllowed(),
                store.getHasParking(),
                store.getOperationStatus(),
                store.getOwnerComment(),
                store.getStoreMainImageUrl(),
                store.getStoreLat(),
                store.getStoreLng(),
                store.getCreatedAt(),
                store.getUpdatedAt(),
                store.getStoreLegalCode(),
                store.getApprovalStatus()
        );
    }

}