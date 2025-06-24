package noodlezip.badge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter

@Embeddable
public class BadgeExtraOption {

    @Column(name = "region_sido", length = 30)
    private String regionSido;

    @Column(name = "ramen_category", length = 50)
    private String ramenCategory;

}
