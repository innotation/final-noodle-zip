package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import noodlezip.common.entity.BaseTimeEntity;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_save_store_category")
public class SaveStoreCategory extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_store_category_id", nullable = false)
    private Long id;

    // 카테고리의 식별용 문자열
    @Size(max = 30)
    @NotNull
    @Column(name = "category_id", nullable = false, length = 30)
    private String categoryId;

    // 카테고리 공개 여부 (true: 공개, false: 비공개)
    @NotNull
    @ColumnDefault("1")
    @Column(name = "public_status", nullable = false)
    private Boolean publicStatus = true;

    // 같은 유저가 여러 카테고리를 가질 때 정렬 기준
    @NotNull
    @Column(name = "category_order", nullable = false)
    private Integer categoryOrder;
}