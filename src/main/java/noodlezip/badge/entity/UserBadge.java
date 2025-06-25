package noodlezip.badge.entity;

import jakarta.persistence.*;
import lombok.*;
import noodlezip.badge.constants.InitBadgeCurrentValue;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.entity.common.BaseTimeEntity;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString

@Entity
@Table(name = "tbl_user_badge")
public class UserBadge extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_badge_id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "badge_id", nullable = false)
    private Badge badge;

    @Column(name = "current_value", nullable = false)
    private Integer currentValue;

    @Enumerated(EnumType.STRING)
    @Column(name = "post_status", nullable = false, length = 30)
    private PostStatusType postStatus;

    @Column(name = "obtained_at")
    private LocalDateTime obtainedAt;


    public void updateCurrentValueByOne() {
        currentValue++;
    }

    public boolean isOverCompletionValue() {
        return badge.isOverCompletionValue(currentValue);
    }

    public UserBadge getNextUserDefaultBadge() {
        Long nextLevelBadgeId = badge.getNextLevelBadgeId();
        if (nextLevelBadgeId == null) {
            return null;
        }

        Badge newLevelBadge = Badge.builder()
                .id(nextLevelBadgeId)
                .build();
        return UserBadge.builder()
                .userId(userId)
                .badge(newLevelBadge)
                .currentValue(InitBadgeCurrentValue.NEXT_LEVEL.getValue())
                .postStatus(PostStatusType.VISIBLE)
                .build();
    }

}