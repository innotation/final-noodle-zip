package noodlezip.badge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import noodlezip.ramen.entity.Category;

@AllArgsConstructor
@NoArgsConstructor
@Getter

@Embeddable
public class BadgeExtraOption {

    @Column(name = "store_sido_legal_code")
    private Integer storeSidoLegalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ramen_category_id")
    private Category ramenCategory;
    //조회의 목적이 압도적으로많기 떄문에 그냥 LONG 타입으로 넣고 마이페이지 출력할때만 findbyid?

}