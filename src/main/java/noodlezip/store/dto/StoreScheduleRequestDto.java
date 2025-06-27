package noodlezip.store.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreScheduleRequestDto {

    private String dayOfWeek; // 요일

    @DateTimeFormat(pattern = "HH:mm") // 오픈 시간
    private LocalTime openingAt;

    @DateTimeFormat(pattern = "HH:mm") // 마감 시간
    private LocalTime closingAt;

    private Boolean isClosedDay; // 휴무일
}