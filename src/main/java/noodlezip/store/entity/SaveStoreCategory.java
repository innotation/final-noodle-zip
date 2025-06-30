package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_save_store_category")
public class SaveStoreCategory extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_store_category_id", nullable = false)
    private Long id;

    @Size(max = 30)
    @NotNull
    @Column(name = "category_id", nullable = false, length = 30)
    private String categoryId;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "public_status", nullable = false)
    private Boolean publicStatus = false;

    @NotNull
    @Column(name = "category_order", nullable = false)
    private Integer categoryOrder;
}