package noodlezip.badge.service.process.level.handler;

import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.entity.UserBadge;
import org.springframework.stereotype.Component;

@Component
public class LevelValueUpdateHandler { //인터페이스로 놔도 될듯,

    public boolean handle(UserBadge userBadge, BadgeStrategyType strategy) {
        if (strategy.equals(BadgeStrategyType.LEVEL)) {
            return updateLevelTypeBadgeValue(userBadge);
        } else if (strategy.equals(BadgeStrategyType.LEVEL_ACCUMULATIVE)) {
            return updateLevelAccumulativeTypeBadgeValue(userBadge);
        }
        return false;
    }

    private boolean updateLevelAccumulativeTypeBadgeValue(UserBadge userBadge) {
        userBadge.updateAccumulativeValueByOne();
        if (!userBadge.isFinalBadgeAlreadyObtained()) {
            userBadge.updateCurrentValueByOne();
            return true;
        }
        return false;
    }

    private boolean updateLevelTypeBadgeValue(UserBadge userBadge) {
        if (userBadge.isFinalBadgeAlreadyObtained()) {
            return false;
        }
        userBadge.updateCurrentValueByOne();
        return true;
    }

}
