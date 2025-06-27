package noodlezip.ramen.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class RamenToppingId implements Serializable {
    private static final long serialVersionUID = 5173735898124235499L;
    @NotNull
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @NotNull
    @Column(name = "topping_id", nullable = false)
    private Long toppingId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        RamenToppingId entity = (RamenToppingId) o;
        return Objects.equals(this.menuId, entity.menuId) &&
                Objects.equals(this.toppingId, entity.toppingId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, toppingId);
    }

}