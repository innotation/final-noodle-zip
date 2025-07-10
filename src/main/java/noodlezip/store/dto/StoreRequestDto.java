package noodlezip.store.dto;

import lombok.*;
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
    private List<StoreScheduleRequestDto> weekSchedule;

    private String storeMainImageUrl;
    private MultipartFile storeMainImage;

    private Long storeLegalCode;
}