package noodlezip.mypage.dto.response.savedstore;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SavedStoreResponse {

    private Long savedStoreId;
    private Long saveStoreCategoryId;
    private Long storeId;
    private String storeName;
    private String address;
    private String storeMainImageUrl;
    private String memo;

}
