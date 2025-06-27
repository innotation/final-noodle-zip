package noodlezip.ramen.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_ramen_topping")
public class RamenTopping {
    @EmbeddedId
    private RamenToppingId id;

    //TODO [Reverse Engineering] generate columns from DB
}