package noodlezip.store.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreScheduleRequestDto {

    private String dayOfWeek; // 요일

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime openingAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss")
        private LocalTime closingAt;

    private Boolean isClosedDay; // 휴무일

    public static StoreScheduleRequestDto fromEntity(noodlezip.store.entity.StoreWeekSchedule schedule) {
        return StoreScheduleRequestDto.builder()
                .dayOfWeek(schedule.getId().getDayOfWeek())
                .openingAt(schedule.getOpeningAt())
                .closingAt(schedule.getClosingAt())
                .isClosedDay(schedule.getIsClosedDay())
                .build();
    }
}