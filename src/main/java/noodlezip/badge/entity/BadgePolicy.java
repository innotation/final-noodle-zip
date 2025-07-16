package noodlezip.badge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder

@Embeddable
public class BadgePolicy {

    @Column(name = "badge_level")
    private Integer badgeLevel;

    @Column(name = "completion_value", nullable = false)
    private Integer completionValue;

    @Column(name = "next_badge_id")
    private Long nextBadgeId;


    public boolean isOverCompletionValue(int currentValue) {
        return completionValue <= currentValue;
    }

    public boolean hasNothingNextBadge() {
        return nextBadgeId == null;
    }

    public boolean hasNotLevel() {
        return badgeLevel == null;
    }

}
