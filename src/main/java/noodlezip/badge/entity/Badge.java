package noodlezip.badge.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.badge.entity.common.BaseTimeEntity;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "tbl_badge")
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

    @Column(name = "active_status", nullable = false, length = 30)
    private String activeStatus;

    @Column(name = "badge_image_url", length = 500)
    private String badgeImageUrl;

}