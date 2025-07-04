package noodlezip.savedstore.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "tbl_save_store_category")
public class SaveStoreCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_store_category_id", nullable = false)
    private Long id;

    @Setter
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Setter
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "category_order", nullable = false)
    private Integer categoryOrder;

    @OneToMany(mappedBy = "saveStoreCategory", fetch = FetchType.LAZY)
    private List<SaveStore> saveStores;
}
