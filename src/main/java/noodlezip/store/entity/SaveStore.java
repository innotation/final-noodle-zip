package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import noodlezip.users.entity.User;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "tbl_save_store")
public class SaveStore {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "save_store_category_id", nullable = false)
    private SaveStoreCategory saveStoreCategory;

    @Size(max = 300)
    @Column(name = "memo", length = 300)
    private String memo;

    @NotNull
    @Column(name = "x_axis", nullable = false)
    private Double xAxis;

    @NotNull
    @Column(name = "y_axis", nullable = false)
    private Double yAxis;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;
}