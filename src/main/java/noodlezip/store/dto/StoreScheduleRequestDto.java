package noodlezip.store.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreScheduleRequestDto {

    private String dayOfWeek; // 요일

    private LocalDateTime openingAt; // 오픈 시간

    private LocalDateTime closingAt; // 마감 시간

    private Boolean isClosedDay; // 휴무일
}