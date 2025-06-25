package noodlezip.store.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreScheduleRequestDto {

    private String dayOfWeek;       // 요일 (MONDAY, TUESDAY 등)
    private String openingAt;       // 오픈 시간 (예: "10:00")
    private String closingAt;       // 마감 시간 (예: "22:00")
    private Boolean isClosedDay;    // 휴무 여부
}
