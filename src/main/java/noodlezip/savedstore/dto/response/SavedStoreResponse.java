package noodlezip.savedstore.dto.response;

import lombok.*;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SavedStoreResponse {

    private Long savedStoreId;
    private Long saveStoreCategoryId;
    private String saveStoreCategoryName;
    private Long storeId;
    private String storeName;
    private String address;
    private String storeMainImageUrl;
    private String memo;

    private Double storeLat;
    private Double storeLng;


    public SavedStoreResponse(Long savedStoreId,
                              Long saveStoreCategoryId,
                              String saveStoreCategoryName,
                              Long storeId,
                              String storeName,
                              String address,
                              String storeMainImageUrl,
                              String memo,
                              Double storeLat,
                              Double storeLng
    ) {
        this.savedStoreId = savedStoreId;
        this.saveStoreCategoryId = saveStoreCategoryId;
        this.saveStoreCategoryName = saveStoreCategoryName;
        this.storeId = storeId;
        this.storeName = storeName;
        this.address = address;
        this.storeMainImageUrl = storeMainImageUrl;
        this.memo = memo;
        this.storeLat = storeLat;
        this.storeLng = storeLng;
    }

    public SavedStoreResponse(Long savedStoreId,
                              Long saveStoreCategoryId,
                              String saveStoreCategoryName,
                              Long storeId,
                              String storeName,
                              String address,
                              String storeMainImageUrl,
                              String memo
    ) {
        this.savedStoreId = savedStoreId;
        this.saveStoreCategoryId = saveStoreCategoryId;
        this.saveStoreCategoryName = saveStoreCategoryName;
        this.storeId = storeId;
        this.address = address;
        this.storeName = storeName;
        this.storeMainImageUrl = storeMainImageUrl;
        this.memo = memo;
    }

}
