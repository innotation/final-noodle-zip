package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.ramen.entity.Category;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tbl_menu")
public class Menu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private Long storeId;

    @Size(max = 30)
    @NotNull
    private String menuName;

    @NotNull
    private Integer price;

    @Size(max = 300)
    private String menuDescription;

    @Size(max = 500)
    private String menuImageUrl;

    @NotNull
    private Integer ramenCategoryId;

    @NotNull
    private Integer ramenSoupId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ramen_category_id", insertable = false, updatable = false)
    private Category category;
}