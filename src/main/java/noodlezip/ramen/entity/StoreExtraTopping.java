package noodlezip.ramen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_store_extra_topping")
public class StoreExtraTopping {
    @Id
    @Column(name = "store_extra_topping_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @NotNull
    @Column(name = "topping_id", nullable = false)
    private Long toppingId;

    @Column(name = "price")
    private Integer price;

}