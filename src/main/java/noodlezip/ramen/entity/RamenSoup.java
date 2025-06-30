package noodlezip.ramen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;

import java.time.Instant;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_ramen_soup")
public class RamenSoup extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ramen_soup_id", nullable = false)
    private Integer id;

    @Size(max = 30)
    @NotNull
    @Column(name = "soup_name", nullable = false, length = 30)
    private String soupName;

}