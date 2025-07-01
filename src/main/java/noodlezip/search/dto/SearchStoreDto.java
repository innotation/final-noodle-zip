package noodlezip.search.dto;

import lombok.*;

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
    private String hasParking;
    private String ownerComment;
    private String storeMainImageUrl;
    private Double storeLat;
    private Double storeLng;
    private Double distance;

}
