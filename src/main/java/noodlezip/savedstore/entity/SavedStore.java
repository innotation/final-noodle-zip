package noodlezip.savedstore.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.common.entity.BaseTimeEntity;
import noodlezip.store.entity.Store;
import noodlezip.user.entity.User;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "tbl_save_store")
public class SavedStore extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_store_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "save_store_category_id", nullable = false)
    private SavedStoreCategory saveStoreCategory;

    @Setter
    @Column(name = "memo", nullable = false)
    private String memo;

    @Embedded
    private SavedStoreLocation location;

}
