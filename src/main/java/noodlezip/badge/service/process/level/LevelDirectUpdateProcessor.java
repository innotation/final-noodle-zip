package noodlezip.badge.service.process.level;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.BadgeCategory;
import noodlezip.badge.entity.UserBadge;
import noodlezip.badge.repository.*;
import noodlezip.badge.service.process.level.handler.LevelUpHandler;
import noodlezip.badge.service.process.level.handler.ValueUpdateHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class LevelDirectUpdateProcessor {

    private final BadgeCategoryRepository badgeCategoryRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final ValueUpdateHandler levelValueUpdateHandler;
    private final LevelUpHandler levelUpHandler;

    @Transactional //todo 없애야됨
    public void process(Long userId, LevelBadgeCategoryType badgeCategoryType) {
        Long badgeCategoryId = badgeCategoryType.getDbPk();
        BadgeStrategyType badgeStrategy = getBadgeStrategyType(badgeCategoryId);
        Optional<UserBadge> nowUserBadge = userBadgeRepository.findUserLevelBadge(userId, badgeCategoryId);

        if (nowUserBadge.isEmpty()) {
            createInitialUserBadge(userId, badgeCategoryId, badgeStrategy);
            return;
        }
        UserBadge userBadge = nowUserBadge.get();
        if (!levelValueUpdateHandler.handle(userBadge, badgeStrategy)) {
            return;
        }
        levelUpHandler.handle(userBadge, badgeStrategy);
    }


    private void createInitialUserBadge(Long userId, Long badgeCategoryId, BadgeStrategyType badgeStrategy) {
        UserBadge newBadge = getInitBadge(userId, badgeCategoryId, badgeStrategy);
        userBadgeRepository.save(newBadge);
        if (newBadge.isOverCompletionValue()) {
            levelUpHandler.handle(newBadge, badgeStrategy);
        }
    }


    private UserBadge getInitBadge(Long userId, Long badgeCategoryId, BadgeStrategyType badgeStrategy) {
        Badge minLevelBAdge = badgeRepository.findInitLevelBadge(badgeCategoryId).get(); //todo 예외처리 -> 없을 경우

        return UserBadge.builder()
                .userId(userId)
                .badge(minLevelBAdge)
                .currentValue(badgeStrategy.getInitCurrentValue())
                .accumulativeValue(badgeStrategy.getInitAccumulativeValue())
                .postStatus(PostStatusType.VISIBLE)
                .build();
    }

    private BadgeStrategyType getBadgeStrategyType(Long badgeCategoryId) {
        BadgeCategory badgeCategory = badgeCategoryRepository.findById(badgeCategoryId).orElse(null); //todo 예외처리 -> 없을 경우
        return badgeCategory.getBadgeStrategy();
    }

}
