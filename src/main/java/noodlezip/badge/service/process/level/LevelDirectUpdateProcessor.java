package noodlezip.badge.service.process.level;

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
import noodlezip.badge.service.process.level.handler.LevelUpHandler;
import noodlezip.badge.service.process.level.handler.LevelValueUpdateHandler;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LevelDirectUpdateProcessor {

    private final BadgeCategoryRepository badgeCategoryRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    private final LevelValueUpdateHandler levelValueUpdateHandler;
    private final LevelUpHandler levelUpHandler;

    @Transactional
    public void process(Long userId, LevelBadgeCategoryType badgeCategoryType) {
        ///  배지 찾음 - 다름
        Long badgeCategoryId = badgeCategoryType.getDbPk();
        List<UserBadge> foundBadge = getUserBadges(userId, badgeCategoryId);
        BadgeCategory badgeCategory = badgeCategoryRepository.findById(badgeCategoryId).orElse(null); //todo 예외처리
        BadgeStrategyType badgeStrategy = badgeCategory.getBadgeStrategy();
        
        /// 초기화 - 다름
        if (foundBadge.isEmpty()) {
            createInitialUserBadge(userId, badgeCategoryId, badgeStrategy); //초기화만 하지말고 같은지도 확인해애함
            return;
        }

        UserBadge userBadge = foundBadge.get(0);

        /// value 업데이트 - 전략에 따라서 - 같음
        if (!levelValueUpdateHandler.handle(userBadge, badgeStrategy)) {
            return;
        }
        /// 배지 레벨 업데이트 - 다름
        levelUpHandler.handle(userBadge, badgeStrategy);
    }

    private List<UserBadge> getUserBadges(Long userId, Long badgeCategoryId) {
        return userBadgeRepository.findByUserIdAndBadgeCategoryIdWithBadge(
                userId,
                badgeCategoryId,
                PageRequest.of(0, 1)
        );
    }

    /// 다름
    private void createInitialUserBadge(Long userId, Long badgeCategoryId, BadgeStrategyType badgeStrategy) {
        UserBadge newBadge = getInitBadge(userId, badgeCategoryId, badgeStrategy);
        userBadgeRepository.save(newBadge);
        if (newBadge.isOverCompletionValue()) {
            levelUpHandler.handle(newBadge, badgeStrategy);
        }
    }


    /// 다름
    private UserBadge getInitBadge(Long userId, Long badgeCategoryId, BadgeStrategyType badgeStrategy) {
        List<Badge> foundBadge = badgeRepository.findMinLevelBadgeByBadgeCategoryId(
                badgeCategoryId,
                PageRequest.of(0, 1)
        );

        Badge minLevelBadge = foundBadge.get(0);

        return UserBadge.builder()
                .userId(userId)
                .badge(minLevelBadge)
                .currentValue(badgeStrategy.getInitCurrentValue())
                .accumulativeValue(badgeStrategy.getInitAccumulativeValue())
                .postStatus(PostStatusType.VISIBLE)
                .build();
    }
















//    /// 분리 가능 - 라멘, 카테고리에서도 쓰일듯 옵션 하나가 더 들어간거라 배지를 찾는 로직과 다름 레벨로 업데이트 하는로직만 다를것이더.
//    private boolean updateBadgeValue(UserBadge userBadge, BadgeStrategyType strategy) {
//        if (strategy.equals(BadgeStrategyType.LEVEL)) {
//            return updateLevelTypeBadgeValue(userBadge);
//        } else if (strategy.equals(BadgeStrategyType.LEVEL_ACCUMULATIVE)) {
//            return updateLevelAccumulativeTypeBadgeValue(userBadge);
//        }
//        return false;
//    }
//
//    /// 분리 가능
//    private boolean updateLevelAccumulativeTypeBadgeValue(UserBadge userBadge) {
//        userBadge.updateAccumulativeValueByOne();
//        if (!userBadge.isFinalBadgeAlreadyObtained()) {
//            userBadge.updateCurrentValueByOne();
//            return true;
//        }
//        return false;
//    }
//
//    /// 분리 가능
//    private boolean updateLevelTypeBadgeValue(UserBadge userBadge) {
//        if (userBadge.isFinalBadgeAlreadyObtained()) { /// 이거까지 도달하는게 너ㅜㅁ 길다
//            return false;
//        }
//        userBadge.updateCurrentValueByOne();
//        return true;
//    }









//
//    /// 다 찻으면 다름 아니면 리턴
//    private void handleBadgeLevelUpdate(UserBadge userBadge, BadgeStrategyType badgeStrategy) {
//        if (userBadge.isOverCompletionValue()) {
//            userBadge.obtain();
//        }
//        if (userBadge.isNextBadgeUpgradable()) {
//            createNextLevelBadge(userBadge, badgeStrategy);
//        }
//    }
//    private void createNextLevelBadge(UserBadge userBadge, BadgeStrategyType badgeStrategy) {
//        UserBadge newLevelUserBadge = userBadge.getNextLevelUserDefaultBadge(badgeStrategy);
//        userBadgeRepository.save(newLevelUserBadge);
//    }
//
//
//

}
