package noodlezip.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class StoreWeekScheduleId implements Serializable {

    @NotNull
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Size(max = 10)
    @NotNull
    @Column(name = "day_of_week", nullable = false, length = 10)
    private String dayOfWeek;
}
