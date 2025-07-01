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
@Table(name = "tbl_badge_group")
public class BadgeGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_group_id", nullable = false)
    private Long id;

    @Column(name = "badge_group_name", nullable = false, length = 30)
    private String badgeGroupName;

    @OneToMany(mappedBy = "badgeGroup", fetch = FetchType.LAZY)
    private List<BadgeCategory> badgeCategoryList;

}