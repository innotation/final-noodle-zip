package noodlezip.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class RamenToppingId implements Serializable {
    @Column(name="menu_id", nullable = false)
    private Long menuId;
    @Column(name="topping_id", nullable = false)
    private Long toppingId;
}