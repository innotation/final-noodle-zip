package noodlezip.badge.service.process.level.handler;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.UserBadge;
import noodlezip.badge.repository.BadgeRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LevelUpHandler {

    private final EntityManager entityManager;
    private final UserBadgeRepository userBadgeRepository;

    public void handle(UserBadge userBadge, BadgeStrategyType badgeStrategy) {
        if (userBadge.isOverCompletionValue()) {
            userBadge.obtain();
        }
        if (userBadge.isNextBadgeUpgradable()) {
            createNextLevelBadge(userBadge, badgeStrategy);
        }
    }

    private void createNextLevelBadge(UserBadge userBadge, BadgeStrategyType badgeStrategy) {
        UserBadge newLevelUserBadge = getNextLevelUserDefaultBadge(userBadge, badgeStrategy);
        userBadgeRepository.save(newLevelUserBadge);
    }

    private UserBadge getNextLevelUserDefaultBadge(UserBadge userBadge, BadgeStrategyType strategy) {
        Long nextLevelBadgeId = userBadge.getBadge().getNextLevelBadgeId();
        Integer accumulativeValue = userBadge.getAccumulativeValue();
        Badge nextLevelBadge = entityManager.getReference(Badge.class, nextLevelBadgeId);

        return UserBadge.builder()
                .userId(userBadge.getUserId())
                .badge(nextLevelBadge)
                .currentValue(strategy.getInitCurrentValueForNextLevel())
                .postStatus(PostStatusType.VISIBLE)
                .accumulativeValue(accumulativeValue)
                .build();
    }

}
