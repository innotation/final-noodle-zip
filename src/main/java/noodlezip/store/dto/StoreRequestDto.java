package noodlezip.store.dto;

import lombok.*;
import noodlezip.store.constant.ApprovalStatus;
import noodlezip.store.constant.OperationStatus;
import noodlezip.store.constant.ParkingType;

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
    private ParkingType hasParking;   // 무료/유료/불가 (Enum)

    private String ownerComment;      // 사장님 한 줄 소개

    private Double storeLat; // 위도
    private Double storeLng; // 경도

    private ApprovalStatus approvalStatus;   // 승인 상태 (WAITING, APPROVED) (Enum)
    private OperationStatus operationStatus;  // 운영 상태 (OPEN, CLOSED) (Enum)

    private List<MenuRequestDto> menus; // 메뉴 리스트 포함(메뉴 엔티티로 매핑)
    private List<StoreScheduleRequestDto> weekSchedule;  // 요일별 영업시간

    private String storeMainImageUrl; // 이미지저장 url

    // userId는 서버에서 받아와서 Entity에 매핑해야함
}