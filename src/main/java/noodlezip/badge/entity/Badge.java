package noodlezip.badge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import noodlezip.common.entity.BaseTimeEntity;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "tbl_badge",
        indexes = {
                @Index(name = "idx_badge_category_level", columnList = "badge_category_id, badge_level"),
                @Index(name = "idx_badge_category_ramen_level", columnList = "badge_category_id, ramen_category_id, badge_level"),
                @Index(name = "idx_badge_category_sido_level", columnList = "badge_category_id, store_sido_legal_code, badge_level")
        }
)
public class Badge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id", nullable = false)
    private Long id;

    @Column(name = "badge_name", nullable = false, length = 100)
    private String badgeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_category_id", nullable = false)
    private BadgeCategory badgeCategory;

    @Embedded
    private BadgePolicy badgePolicy;

    @Embedded
    private BadgeExtraOption badgeExtraOption;
//
//    @Column(name = "is_active", nullable = false)
//    private Boolean isActive;

    @Column(name = "badge_image_url", length = 500)
    private String badgeImageUrl;


    public boolean isOverCompletionValue(int currentValue) {
        return badgePolicy.isOverCompletionValue(currentValue);
    }

    public Long getNextLevelBadgeId() {
        return badgePolicy.getNextBadgeId();
    }

    public boolean hasNothingNextBadge() {
        return badgePolicy.hasNothingNextBadge();
    }

}