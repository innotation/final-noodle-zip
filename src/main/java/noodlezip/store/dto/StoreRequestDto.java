package noodlezip.store.dto;

import jakarta.validation.constraints.Size;
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

    @Size(max = 200, message = "사장님 한 줄 소개는 200자 이하로 작성해주세요.")
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
}