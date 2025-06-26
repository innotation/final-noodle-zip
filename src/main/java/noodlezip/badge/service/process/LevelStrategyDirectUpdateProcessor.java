package noodlezip.badge.service.process;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.BadgeCategory;
import noodlezip.badge.entity.UserBadge;
import noodlezip.badge.repository.BadgeCategoryRepository;
import noodlezip.badge.repository.BadgeRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LevelStrategyDirectUpdateProcessor {

    private final BadgeCategoryRepository badgeCategoryRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    public void process(Long userId, LevelBadgeCategoryType badgeCategoryType) {
        Long badgeCategoryId = badgeCategoryType.getDbPk();

        List<UserBadge> foundBadge = userBadgeRepository.findByUserIdAndBadgeCategoryIdWithBadge(
                userId,
                badgeCategoryId,
                PageRequest.of(0, 1)
        );
        BadgeCategory badgeCategory = badgeCategoryRepository.findById(badgeCategoryId).orElse(null);
        BadgeStrategyType badgeStrategy = badgeCategory.getBadgeStrategy();

        if (foundBadge.isEmpty()) {
            makeInitBadge(userId, badgeCategoryId, badgeStrategy);
            return;
        }

        UserBadge userBadge = foundBadge.get(0);


        if (BadgeStrategyType.LEVEL.equals(badgeStrategy)) {
            if (userBadge.isUnableUpdateNextBadge()) {
                return;
            }
            userBadge.updateCurrentValueByOne();
            /// 레벨업 확인
        }

        if (BadgeStrategyType.LEVEL_ACCUMULATIVE.equals(badgeStrategy)) {
            userBadge.updateAccumulativeValueByOne();
            if (userBadge.isUnableUpdateNextBadge()) {
                userBadgeRepository.save(userBadge);
                return;
            }
            userBadge.updateAccumulativeValueByOne();
        }

        userBadgeRepository.save(userBadge);

        if (userBadge.isOverCompletionValue() && !userBadge.isUnableUpdateNextBadge()) {
            userBadge.obtain();
            UserBadge newLevelUserBadge = userBadge.getNextLevelUserDefaultBadge(badgeStrategy);

            userBadgeRepository.save(newLevelUserBadge);
        }

    }

    private boolean updateBadgeValue(UserBadge userBadge, BadgeStrategyType strategy) {
        if (strategy.equals(BadgeStrategyType.LEVEL)) {
            return updateLevelTypeBadgeValue(userBadge);
        } else if (strategy.equals(BadgeStrategyType.LEVEL_ACCUMULATIVE)) {
            return updateLevelAccumulativeTypeBadgeValue(userBadge);
        }
        return false;
    }

    private boolean updateLevelAccumulativeTypeBadgeValue(UserBadge userBadge) {
        userBadge.updateAccumulativeValueByOne();
        if (userBadge.isUnableUpdateNextBadge()) {
            userBadge.updateAccumulativeValueByOne();
        }
        return true;
    }

    private boolean updateLevelTypeBadgeValue(UserBadge userBadge) {
        if (userBadge.isUnableUpdateNextBadge()) {
            return false;
        }
        userBadge.updateCurrentValueByOne();
        return true;
    }


    private void makeInitBadge(Long userId, Long badgeCategoryId, BadgeStrategyType badgeStrategy) {
        List<Long> foundBadge = badgeRepository.findMinLevelBadgeByBadgeCategoryId(
                badgeCategoryId,
                PageRequest.of(0, 1)
        );

        Long minLevelBadgeId = foundBadge.get(0);
        Badge minLevelBadge = Badge.builder()
                .id(minLevelBadgeId)
                .build();

        UserBadge newBadge = UserBadge.builder()
                .userId(userId)
                .badge(minLevelBadge)
                .currentValue(badgeStrategy.getInitCurrentValue())
                .accumulativeValue(badgeStrategy.getInitAccumulativeValue())
                .postStatus(PostStatusType.VISIBLE)
                .build();

        userBadgeRepository.save(newBadge);
    }

}
