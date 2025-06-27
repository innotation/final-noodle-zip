package noodlezip.ramen.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.store.entity.Menu;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "tbl_ramen_category")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ramen_category_id")
    private Integer id;

    @Column(name = "category_name", nullable = false, length = 50)
    private String name;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private List<Menu> menus;
}
