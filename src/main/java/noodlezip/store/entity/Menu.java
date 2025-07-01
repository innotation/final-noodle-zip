package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;
import noodlezip.ramen.entity.Category;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_menu")
public class Menu extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "menu_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "store_id", nullable = false)
    private Long storeId;

    @Size(max = 30)
    @NotNull
    @Column(name = "menu_name", nullable = false, length = 30)
    private String menuName;

    @NotNull
    @Column(name = "price", nullable = false)
    private Integer price;

    @Size(max = 300)
    @Column(name = "menu_description", length = 300)
    private String menuDescription;

    @Size(max = 500)
    @Column(name = "menu_image_url", length = 500)
    private String menuImageUrl;

    @NotNull
    @Column(name = "ramen_category_id", nullable = false)
    private Integer ramenCategoryId;

    @NotNull
    @Column(name = "ramen_soup_id", nullable = false)
    private Integer ramenSoupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ramen_category_id", insertable = false, updatable = false)
    private Category category;
}