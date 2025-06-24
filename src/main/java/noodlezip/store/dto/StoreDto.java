package noodlezip.store.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.io.Serializable;
import java.time.Instant;

/**
 * DTO for {@link noodlezip.store.entity.Store}
 */
@Value
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
    Double xAxis;
    @NotNull
    Double yAxis;
    @NotNull
    Instant createdAt;
    Instant updatedAt;
    Integer bCode;
}