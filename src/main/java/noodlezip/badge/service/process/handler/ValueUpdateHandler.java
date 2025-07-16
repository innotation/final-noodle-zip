package noodlezip.badge.service.process.handler;

import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.entity.UserBadge;
import org.springframework.stereotype.Component;

@Component
public class ValueUpdateHandler {

    public boolean handle(UserBadge userBadge, BadgeStrategyType strategy) {
        if (strategy.equals(BadgeStrategyType.LEVEL)) {
            return updateLevelTypeBadgeValue(userBadge);
        } else if (strategy.equals(BadgeStrategyType.LEVEL_ACCUMULATIVE)) {
            return updateLevelAccumulativeTypeBadgeValue(userBadge);
        }
        return false;
    }

    public boolean updateLevelAccumulativeTypeBadgeValue(UserBadge userBadge) {
        userBadge.updateAccumulativeValueByOne();
        if (!userBadge.isFinalBadgeAlreadyObtained()) {
            userBadge.updateCurrentValueByOne();
            return true;
        }
        return false;
    }

    public boolean updateLevelTypeBadgeValue(UserBadge userBadge) {
        if (userBadge.isFinalBadgeAlreadyObtained()) {
            return false;
        }
        userBadge.updateCurrentValueByOne();
        return true;
    }

}
