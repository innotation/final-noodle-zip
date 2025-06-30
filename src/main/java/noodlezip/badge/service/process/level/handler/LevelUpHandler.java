package noodlezip.badge.service.process.level.handler;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.entity.UserBadge;
import noodlezip.badge.repository.UserBadgeRepository;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class LevelUpHandler {

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
        UserBadge newLevelUserBadge = userBadge.getNextLevelUserDefaultBadge(badgeStrategy);
        userBadgeRepository.save(newLevelUserBadge);
    }

}
