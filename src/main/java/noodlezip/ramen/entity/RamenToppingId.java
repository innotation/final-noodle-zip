package noodlezip.ramen.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.store.entity.Menu;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@EqualsAndHashCode
public class RamenToppingId /*implements Serializable*/ {
    @Column(name="menu_id", nullable = false)
    private Long menuId;
    @Column(name="topping_id", nullable = false)
    private Long toppingId;

}