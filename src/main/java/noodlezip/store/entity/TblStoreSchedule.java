package noodlezip.store.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_store_week_schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TblStoreSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Column(length = 10, nullable = false)
    private String dayOfWeek;

    private String openingAt;
    private String closingAt;

    private Boolean isClosedDay;
}

