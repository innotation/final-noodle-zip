package noodlezip.ramen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_ramen_soup")
public class RamenSoup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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