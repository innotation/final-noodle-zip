package noodlezip.savedstore.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;
import noodlezip.user.entity.User;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "tbl_save_store_category")
public class SavedStoreCategory extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_store_category_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Setter
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "category_order", nullable = false)
    private Integer categoryOrder;

    @OneToMany(mappedBy = "saveStoreCategory", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SavedStore> saveStoreList;


    public boolean validateOwner(Long userId) {
        return this.user.getId().equals(userId);
    }

}
