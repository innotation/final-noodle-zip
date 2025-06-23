import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import noodlezip.store.dto.WeekScheduleDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreRequestDto {
    private String storeName;
    private String address;
    private String phone;
    private Boolean isLocalCard;
    private Boolean isChildAllowed;
    private String hasParking;
    private String operationStatus;
    private String ownerComment;
    private String storeMainImageUrl;
    private Double xAxis;
    private Double yAxis;
    private List<WeekScheduleDto> weekSchedules;
}
