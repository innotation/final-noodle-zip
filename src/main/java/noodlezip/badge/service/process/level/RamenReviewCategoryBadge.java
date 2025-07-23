package noodlezip.badge.service.process.level;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.UserBadge;
import noodlezip.badge.repository.BadgeCategoryRepository;
import noodlezip.badge.repository.BadgeRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import noodlezip.badge.service.process.handler.LevelUpHandler;
import noodlezip.badge.service.process.handler.ValueUpdateHandler;
import noodlezip.badge.status.BadgeErrorStatus;
import noodlezip.common.exception.CustomException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RamenReviewCategoryBadge {

    private final static List<Integer> validRamenCategoryIdList = List.of(1, 2, 3, 4, 5, 6, 12);

    private final BadgeCategoryRepository badgeCategoryRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;
    private final ValueUpdateHandler levelValueUpdateHandler;
    private final LevelUpHandler levelUpHandler;

    @Transactional
    public void process(Long userId, int ramenCategoryId) {
        if (!isValidRamenCategory(ramenCategoryId)) {
            return;
        }
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();
        if (!badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)) {
            return;
        }
        BadgeStrategyType badgeStrategy = BadgeStrategyType.LEVEL_ACCUMULATIVE;
        Optional<UserBadge> nowUserBadge = userBadgeRepository.findRamenCategoryLevelUserBadge(
                userId,
                badgeCategoryId,
                ramenCategoryId
        );
        if (nowUserBadge.isEmpty()) {
            createInitialUserBadge(userId, badgeCategoryId, ramenCategoryId, badgeStrategy);
            return;
        }
        UserBadge userBadge = nowUserBadge.get();
        if (!levelValueUpdateHandler.updateLevelAccumulativeTypeBadgeValue(userBadge)) {
            return;
        }
        levelUpHandler.handle(userBadge, badgeStrategy);
    }


    private void createInitialUserBadge(Long userId,
                                        Long badgeCategoryId,
                                        int ramenCategoryId,
                                        BadgeStrategyType badgeStrategy
    ) {
        UserBadge newBadge = getInitBadge(userId, badgeCategoryId, ramenCategoryId, badgeStrategy);
        userBadgeRepository.save(newBadge);
        if (newBadge.isOverCompletionValue()) {
            levelUpHandler.handle(newBadge, badgeStrategy);
        }
    }


    private UserBadge getInitBadge(Long userId,
                                   Long badgeCategoryId,
                                   int ramenCategoryId,
                                   BadgeStrategyType badgeStrategy
    ) {
        Badge minLevelBAdge = badgeRepository.findInitRamenCategoryLevelBadge(badgeCategoryId, ramenCategoryId)
                .orElseThrow(() -> new CustomException(BadgeErrorStatus._NOT_FOUND_BADGE));

        return UserBadge.builder()
                .userId(userId)
                .badge(minLevelBAdge)
                .currentValue(badgeStrategy.getInitCurrentValue())
                .accumulativeValue(badgeStrategy.getInitAccumulativeValue())
                .postStatus(PostStatusType.VISIBLE)
                .build();
    }

    private boolean isValidRamenCategory(int ramenCategoryId) {
        return validRamenCategoryIdList.contains(ramenCategoryId);
    }

}
