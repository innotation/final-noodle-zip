package noodlezip.badge.entity.temp;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter

@Entity
@Table(name = "tbl_ramen_category")
public class TempRamenCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ramen_category_id", nullable = false)
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 50)
    private String categoryName;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

}