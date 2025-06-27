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
public class RamenCategoryLevelReviewBadgeProcessor {

    private final BadgeCategoryRepository badgeCategoryRepository;
    private final BadgeRepository badgeRepository;
    private final UserBadgeRepository userBadgeRepository;

    private final LevelValueUpdateHandler levelValueUpdateHandler;
    private final LevelUpHandler levelUpHandler;

    @Transactional
    public void process(Long userId, Long menuId, LevelBadgeCategoryType badgeCategoryType) {
        /**
         * 메뉴 아이디로 카테고리 아이디 찾아오기
         */
        int ramenCategoryId = 1; //todo 알아오기
        Long badgeCategoryId = badgeCategoryType.getDbPk();
        List<UserBadge> foundBadge = getUserBadges(userId, badgeCategoryId, ramenCategoryId);
        BadgeCategory badgeCategory = badgeCategoryRepository.findById(badgeCategoryId).orElse(null); //todo 예외처리
        BadgeStrategyType badgeStrategy = badgeCategory.getBadgeStrategy();

        if (foundBadge.isEmpty()) {
            createInitialUserBadge(userId, badgeCategoryId, ramenCategoryId, badgeStrategy); //초기화만 하지말고 같은지도 확인해애함
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

    private List<UserBadge> getUserBadges(Long userId,
                                          Long badgeCategoryId,
                                          int ramenCategoryId
    ) {
        return userBadgeRepository.findByUserIdAndBadgeCategoryIdAndRamenCategoryIdWithBadge(
                userId,
                badgeCategoryId,
                ramenCategoryId,
                PageRequest.of(0, 1)
        );
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
                                   int categoryId,
                                   BadgeStrategyType badgeStrategy
    ) {
        List<Badge> foundBadge = badgeRepository.findMinLevelBadgeByBadgeCategoryIdAndRamenCategoryId(
                badgeCategoryId,
                categoryId,
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
