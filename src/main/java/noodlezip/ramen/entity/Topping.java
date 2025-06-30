package noodlezip.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "tbl_topping")
public class Topping {
    @Id
    @Column(name = "topping_id", nullable = false)
    private Long id;

    @Size(max = 50)
    @NotNull
    @Column(name = "topping_name", nullable = false, length = 50)
    private String toppingName;

    @NotNull
    @ColumnDefault("1")
    @Column(name = "is_active", nullable = false)
    private Boolean isActive = false;

}