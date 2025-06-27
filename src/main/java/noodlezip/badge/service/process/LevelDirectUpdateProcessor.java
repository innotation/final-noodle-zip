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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Component
public class LevelDirectUpdateProcessor {

    private final BadgeCategoryRepository badgeCategoryRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    @Transactional
    public void process(Long userId, LevelBadgeCategoryType badgeCategoryType) {
        Long badgeCategoryId = badgeCategoryType.getDbPk();

        List<UserBadge> foundBadge = getUserBadges(userId, badgeCategoryId);
        BadgeCategory badgeCategory = badgeCategoryRepository.findById(badgeCategoryId).orElse(null);
        BadgeStrategyType badgeStrategy = badgeCategory.getBadgeStrategy();

        if (foundBadge.isEmpty()) {
            createInitialUserBadge(userId,badgeCategoryId,badgeStrategy); //초기화만 하지말고 같은지도 확인해애함
            return;
        }

        UserBadge userBadge = foundBadge.get(0);

        if(!updateBadgeValue(userBadge, badgeStrategy)) {
            return;
        }

        handleOverCompletionValueUserBadge(userBadge, badgeStrategy);
    }

    private List<UserBadge> getUserBadges(Long userId, Long badgeCategoryId) {
        List<UserBadge> foundBadge = userBadgeRepository.findByUserIdAndBadgeCategoryIdWithBadge(
                userId,
                badgeCategoryId,
                PageRequest.of(0, 1)
        );
        return foundBadge;
    }

    /// 분리 가능 - 라멘, 카테고리에서도 쓰일듯 옵션 하나가 더 들어간거라 배지를 찾는 로직과 다름 레벨로 업데이트 하는로직만 다를것이더.
    private boolean updateBadgeValue(UserBadge userBadge, BadgeStrategyType strategy) {
        if (strategy.equals(BadgeStrategyType.LEVEL)) {
            return updateLevelTypeBadgeValue(userBadge);
        } else if (strategy.equals(BadgeStrategyType.LEVEL_ACCUMULATIVE)) {
            return updateLevelAccumulativeTypeBadgeValue(userBadge);
        }
        return false;
    }

    /// 분리 가능
    private boolean updateLevelAccumulativeTypeBadgeValue(UserBadge userBadge) {
        userBadge.updateAccumulativeValueByOne();
        if (!userBadge.isFinalBadgeAlreadyObtained()) {
            userBadge.updateCurrentValueByOne();
        }
        return true;
    }

    ///분리 가능
    private boolean updateLevelTypeBadgeValue(UserBadge userBadge) {
        if (userBadge.isFinalBadgeAlreadyObtained()) { /// 이거까지 도달하는게 너ㅜㅁ 길다
            return false;
        }
        userBadge.updateCurrentValueByOne();
        return true;
    }

    /// 중간다리와 마지막까지 같이 사용, 마지맞 부분이 조건문에 걸리지 않아서 업데이트는 되는데 습득일이 안찍힌디ㅏ
    /**
     * 왜 안찍히냐면 마지막 레벨에 잇는 배지는 isOverCompletionValue()는 되지만 !badge.hasNothingNextBadge()이 아니기 떄문이다
     *
     *
     */
    private void handleOverCompletionValueUserBadge(UserBadge userBadge, BadgeStrategyType badgeStrategy) {
        if(userBadge.isOverCompletionValue()) {
            userBadge.obtain();
        }
        createNextLevelBadge(userBadge, badgeStrategy);
    }

    private void createNextLevelBadge(UserBadge userBadge, BadgeStrategyType badgeStrategy) {
        if (userBadge.isNextBadgeUpgradable()) {
            UserBadge newLevelUserBadge = userBadge.getNextLevelUserDefaultBadge(badgeStrategy); //마지막 배지인데, 다음 아이디가 없을 경우 null

            userBadgeRepository.save(newLevelUserBadge);
        }
    }

    private void createInitialUserBadge(Long userId, Long badgeCategoryId, BadgeStrategyType badgeStrategy){
        UserBadge newBadge = getInitBadge(userId, badgeCategoryId, badgeStrategy);
        userBadgeRepository.save(newBadge);
        if(newBadge.isOverCompletionValue()) {
            handleOverCompletionValueUserBadge(newBadge, badgeStrategy);
        }
    }


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

}
