package noodlezip.badge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import noodlezip.ramen.entity.Category;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Embeddable
public class BadgeExtraOption {

    @Column(name = "store_sido_legal_code")
    private Integer storeSidoLegalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ramen_category_id")
    private Category ramenCategory;

}