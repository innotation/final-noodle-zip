package noodlezip.badge.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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

    @Column(name = "event_start_at")
    private LocalDateTime eventStartAt;

    @Column(name = "event_end_at")
    private LocalDateTime eventEndAt;

}
