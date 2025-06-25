package noodlezip.badge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_group_id", nullable = false)
    private BadgeGroup badgeGroup;

    @OneToMany(mappedBy = "badgeCategory", fetch = FetchType.LAZY)
    private List<Badge> badgeList;

    @Column(name = "is_event", nullable = false)
    private Boolean isEvent = false;

}