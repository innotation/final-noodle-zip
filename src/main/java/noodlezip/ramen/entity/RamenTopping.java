package noodlezip.ramen.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "tbl_ramen_topping")
public class RamenTopping {
    @EmbeddedId
    private RamenToppingId toppingId;
}