package noodlezip.store.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class StoreWeekScheduleId implements Serializable {
    private static final long serialVersionUID = 6944364184128480029L;

    @NotNull
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Size(max = 10)
    @NotNull
    @Column(name = "day_of_week", nullable = false, length = 10)
    private String dayOfWeek;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        StoreWeekScheduleId that = (StoreWeekScheduleId) o;
        return Objects.equals(storeId, that.storeId) &&
                Objects.equals(dayOfWeek, that.dayOfWeek);
    }

    @Override
    public int hashCode() {
        return Objects.hash(storeId, dayOfWeek);
    }
}