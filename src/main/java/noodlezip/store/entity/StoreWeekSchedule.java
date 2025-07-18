package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Getter
@Setter
@Entity
@Table(name = "tbl_store_week_schedule")
public class StoreWeekSchedule {
    @EmbeddedId
    private StoreWeekScheduleId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("storeId")
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @NotNull
    @Column(name = "opening_at", nullable = false)
    private LocalTime openingAt;

    @NotNull
    @Column(name = "closing_at", nullable = false)
    private LocalTime closingAt;

    @NotNull
    @Column(name = "is_closed_day", nullable = false)
    private Boolean isClosedDay;
}