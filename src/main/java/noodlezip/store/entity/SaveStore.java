package noodlezip.store.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import noodlezip.common.entity.BaseTimeEntity;
import noodlezip.users.entity.User;

@Getter
@Setter
@Entity
@Table(name = "tbl_save_store")
public class SaveStore extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "save_store_id", nullable = false)
    private Long id;

    // SaveStore 레코드는 특정 User가 소유
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User userId;

    // 유저가 저장한 Store 정보 (다대일관계)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store storeId;

    // 저장 카테고리 분류
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "save_store_category_id", nullable = false)
    private SaveStoreCategory saveStoreCategoryId;

    @Size(max = 300)
    @Column(name = "memo", length = 300)
    private String memo;

    @Column(name = "store_lat", nullable = false)
    private Double storeLat;

    @Column(name = "store_lng", nullable = false)
    private Double storeLng;
}