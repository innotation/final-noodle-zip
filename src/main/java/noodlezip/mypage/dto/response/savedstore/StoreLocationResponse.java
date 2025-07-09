package noodlezip.mypage.dto.response.savedstore;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class StoreLocationResponse {

    private Integer storeId;
    private Integer saveStoreId;
    private Double storeLat;
    private Double storeLng;

}
/**
 * 사용자가 지도보기를 클릭하면 현재 체크되어있는 카테고리의 모든 가게 좌표들을 찍는다.
 * 클릭하면 동적으로 가게 정보를 받아 처리한다.
 */
