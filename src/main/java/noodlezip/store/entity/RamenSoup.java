package noodlezip.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_ramen_soup")
public class RamenSoup {
    @Id
    @Column(name = "ramen_soup_id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "soup_name", nullable = false, length = 30)
    private String soupName;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}