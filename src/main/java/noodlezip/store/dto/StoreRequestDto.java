package noodlezip.store.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {

    private String storeName;
    private String address;
    private String phone;

    private Boolean isLocalCard;      // 지역카드 가능 여부
    private Boolean isChildAllowed;   // 유아동반 가능 여부
    private String hasParking;        // 무료/유료/불가

    private String ownerComment;      // 사장님 한 줄 소개
    private String storeMainImageUrl; // 대표 이미지 URL

    private Double xAxis; // 위도
    private Double yAxis; // 경도

    private List<MenuRequestDto> menus; // 메뉴 리스트 포함
    private List<StoreScheduleRequestDto> weekSchedule;  // 요일별 영업시간

    // userId는 서버에서 받아와서 Entity에 매핑해야함
}
