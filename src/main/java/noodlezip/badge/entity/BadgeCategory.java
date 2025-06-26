package noodlezip.badge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import noodlezip.badge.constants.BadgeStrategyType;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Entity
@Table(name = "tbl_badge_category")
public class BadgeCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_category_id", nullable = false)
    private Long id;

    @Column(name = "badge_category_name", nullable = false, length = 30)
    private String badgeCategoryName;

    @Column(name = "badge_description", length = 100)
    private String badgeDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "badge_strategy", nullable = false)
    private BadgeStrategyType badgeStrategy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_group_id", nullable = false)
    private BadgeGroup badgeGroup;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "event_start_at")
    private LocalDate eventStartAt;

    @Column(name = "event_end_at")
    private LocalDate eventEndAt;

    @OneToMany(mappedBy = "badgeCategory", fetch = FetchType.LAZY)
    private List<Badge> badgeList;

}