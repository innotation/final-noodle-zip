package noodlezip.store.dto;

import lombok.*;
import noodlezip.store.entity.Store;
import noodlezip.store.status.ApprovalStatus;
import noodlezip.store.status.OperationStatus;
import noodlezip.store.status.ParkingType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreRequestDto {
    private Long id;
    private Long userId;

    private String storeName;
    private String address;
    private String phone;

    private Boolean isLocalCard;
    private Boolean isChildAllowed;
    private ParkingType hasParking;

    private String ownerComment;

    private Double storeLat;
    private Double storeLng;

    private ApprovalStatus approvalStatus;
    private OperationStatus operationStatus;

    private List<MenuRequestDto> menus;
    private List<ExtraToppingRequestDto> extraToppings;
    private List<StoreScheduleRequestDto> weekSchedule;

    private String storeMainImageUrl;
    private MultipartFile storeMainImage;

    private Long storeLegalCode;

    private Long bizNum;

    private String phonePrefix;
    private String phonePrefixInput;
    private String phoneRest;
    private String zipcode;
    private String storeDetailInput;
    private String businessRegistrationNumber;

    public static StoreRequestDto fromEntity(Store store) {
        String[] addressParts = store.getAddress().split(",", 2);
        String mainAddress = addressParts[0];
        String detailAddress = addressParts.length > 1 ? addressParts[1] : "";

        return StoreRequestDto.builder()
                .id(store.getId())
                .userId(store.getUserId())
                .storeName(store.getStoreName())
                .address(mainAddress)
                .storeDetailInput(detailAddress)
                .phone(store.getPhone())
                .isLocalCard(store.getIsLocalCard())
                .isChildAllowed(store.getIsChildAllowed())
                .hasParking(store.getHasParking())
                .operationStatus(store.getOperationStatus())
                .approvalStatus(store.getApprovalStatus())
                .ownerComment(store.getOwnerComment())
                .storeMainImageUrl(store.getStoreMainImageUrl())
                .storeLat(store.getStoreLat())
                .storeLng(store.getStoreLng())
                .storeLegalCode(store.getStoreLegalCode())
                .bizNum(store.getBizNum())
                .build();
    }
}