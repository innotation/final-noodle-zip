package noodlezip.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class WeekScheduleDto {
    private String dayOfWeek;
    private LocalDateTime openingAt;
    private LocalDateTime closingAt;
    private Boolean isClosedDay;
}