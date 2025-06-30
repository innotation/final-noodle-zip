package noodlezip.ramen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
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