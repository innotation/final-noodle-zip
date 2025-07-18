package noodlezip.badge.service.process.level;

import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.UserBadge;
import noodlezip.badge.status.BadgeErrorStatus;
import noodlezip.badge.repository.BadgeCategoryRepository;
import noodlezip.badge.repository.BadgeRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import noodlezip.badge.service.process.handler.LevelUpHandler;
import noodlezip.badge.service.process.handler.ValueUpdateHandler;
import noodlezip.common.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RamenReviewCategoryBadgeTest {

    private BadgeCategoryRepository badgeCategoryRepository;
    private BadgeRepository badgeRepository;
    private UserBadgeRepository userBadgeRepository;
    private ValueUpdateHandler levelValueUpdateHandler;
    private LevelUpHandler levelUpHandler;

    private RamenReviewCategoryBadge ramenBadgeProcessor;

    @BeforeEach
    void setUp() {
        badgeCategoryRepository = mock(BadgeCategoryRepository.class);
        badgeRepository = mock(BadgeRepository.class);
        userBadgeRepository = mock(UserBadgeRepository.class);
        levelValueUpdateHandler = mock(ValueUpdateHandler.class);
        levelUpHandler = mock(LevelUpHandler.class);

        ramenBadgeProcessor = new RamenReviewCategoryBadge(
                badgeCategoryRepository,
                badgeRepository,
                userBadgeRepository,
                levelValueUpdateHandler,
                levelUpHandler
        );
    }

    @DisplayName("UserBadge가 없으면 최초로 초기 UserBadge를 생성한다")
    @Test
    void 최초_유저배지없으면_초기생성된다() {
        // given
        Long userId = 1L;
        int ramenCategoryId = 1;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);
        when(userBadgeRepository.findRamenCategoryLevelUserBadge(userId, badgeCategoryId, ramenCategoryId)).thenReturn(Optional.empty());

        Badge badge = mock(Badge.class);
        when(badgeRepository.findInitRamenCategoryLevelBadge(badgeCategoryId, ramenCategoryId)).thenReturn(Optional.of(badge));

        // when
        ramenBadgeProcessor.process(userId, ramenCategoryId);

        // then
        ArgumentCaptor<UserBadge> captor = ArgumentCaptor.forClass(UserBadge.class);
        verify(userBadgeRepository).save(captor.capture());

        UserBadge saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getBadge()).isEqualTo(badge);
        assertThat(saved.getCurrentValue()).isEqualTo(1);
        assertThat(saved.getAccumulativeValue()).isEqualTo(1);
        assertThat(saved.getPostStatus()).isEqualTo(PostStatusType.VISIBLE);

        verify(levelUpHandler, never()).handle(any(), any());
    }

    @DisplayName("기존 UserBadge가 있으면 valueUpdateHandler를 통해 값 증가 후 true면 levelUpHandler를 호출한다")
    @Test
    void 유저배지_존재시_valueUpdateHandler가_true면_levelUpHandler_호출() {
        // given
        Long userId = 1L;
        int ramenCategoryId = 1;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        UserBadge existingBadge = mock(UserBadge.class);
        when(userBadgeRepository.findRamenCategoryLevelUserBadge(userId, badgeCategoryId, ramenCategoryId))
                .thenReturn(Optional.of(existingBadge));

        when(levelValueUpdateHandler.updateLevelAccumulativeTypeBadgeValue(existingBadge)).thenReturn(true);

        // when
        ramenBadgeProcessor.process(userId, ramenCategoryId);

        // then
        verify(levelValueUpdateHandler).updateLevelAccumulativeTypeBadgeValue(existingBadge);
        verify(levelUpHandler).handle(existingBadge, BadgeStrategyType.LEVEL_ACCUMULATIVE);
    }

    @DisplayName("기존 UserBadge가 있지만 valueUpdateHandler가 false면 levelUpHandler를 호출하지 않는다")
    @Test
    void 유저배지_존재시_valueUpdateHandler가_false면_levelUpHandler_호출안됨() {
        // given
        Long userId = 1L;
        int ramenCategoryId = 1;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        UserBadge existingBadge = mock(UserBadge.class);
        when(userBadgeRepository.findRamenCategoryLevelUserBadge(userId, badgeCategoryId, ramenCategoryId))
                .thenReturn(Optional.of(existingBadge));

        when(levelValueUpdateHandler.updateLevelAccumulativeTypeBadgeValue(existingBadge)).thenReturn(false);

        // when
        ramenBadgeProcessor.process(userId, ramenCategoryId);

        // then
        verify(levelValueUpdateHandler).updateLevelAccumulativeTypeBadgeValue(existingBadge);
        verify(levelUpHandler, never()).handle(any(), any());
    }

    @DisplayName("Badge가 없으면 CustomException(_NOT_FOUND_BADGE) 예외를 발생시킨다")
    @Test
    void badge없으면_CustomException_발생() {
        // given
        Long userId = 1L;
        int ramenCategoryId = 1;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);
        when(userBadgeRepository.findRamenCategoryLevelUserBadge(userId, badgeCategoryId, ramenCategoryId)).thenReturn(Optional.empty());
        when(badgeRepository.findInitRamenCategoryLevelBadge(badgeCategoryId, ramenCategoryId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> ramenBadgeProcessor.process(userId, ramenCategoryId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUND_BADGE.getMessage());
    }

    @DisplayName("유효하지 않은 ramenCategoryId(7)일 경우 아무것도 하지 않는다")
    @Test
    void 유효하지않은_ramenCategoryId면_아무것도하지않음() {
        // when
        ramenBadgeProcessor.process(1L, 7);

        // then
        verifyNoInteractions(badgeCategoryRepository, userBadgeRepository, badgeRepository, levelValueUpdateHandler, levelUpHandler);
    }

    @DisplayName("BadgeCategory가 비활성화 상태라면 아무것도 하지 않는다")
    @Test
    void badgeCategory_비활성화시_아무것도하지않음() {
        // given
        Long userId = 1L;
        int ramenCategoryId = 1;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(false);

        // when
        ramenBadgeProcessor.process(userId, ramenCategoryId);

        // then
        verifyNoInteractions(userBadgeRepository, badgeRepository, levelValueUpdateHandler, levelUpHandler);
    }

}