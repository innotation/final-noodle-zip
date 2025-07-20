package noodlezip.search.dto;

import lombok.*;
import noodlezip.store.status.ParkingType;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class SearchStoreDto {

    private Long id;
    private String storeName;
    private String address;
    private String phone;
    private Boolean isLocalCard;
    private Boolean isChildAllowed;
    private ParkingType hasParking;
    private String ownerComment;
    private String storeMainImageUrl;
    private Double storeLat;
    private Double storeLng;
    private Double distance;
    private LocalDateTime createdAt;

}
