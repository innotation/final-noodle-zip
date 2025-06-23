package noodlezip.store.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tbl_store_week_schedule")
public class StoreWeekSchedule {
    @EmbeddedId
    private StoreWeekScheduleId id;

    private LocalDateTime openingAt;
    private LocalDateTime closingAt;
    private Boolean isClosedDay;

    @MapsId("storeId")
    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;
}

