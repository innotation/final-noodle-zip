package noodlezip.badge.service.process.level;

import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.entity.Badge;
import noodlezip.badge.entity.BadgeCategory;
import noodlezip.badge.entity.UserBadge;
import noodlezip.badge.exception.BadgeErrorStatus;
import noodlezip.badge.repository.BadgeCategoryRepository;
import noodlezip.badge.repository.BadgeRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import noodlezip.badge.service.process.level.handler.LevelUpHandler;
import noodlezip.badge.service.process.level.handler.ValueUpdateHandler;
import noodlezip.common.exception.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class LevelDirectUpdateProcessorTest {

    private BadgeCategoryRepository badgeCategoryRepository;
    private BadgeRepository badgeRepository;
    private UserBadgeRepository userBadgeRepository;
    private ValueUpdateHandler levelValueUpdateHandler;
    private LevelUpHandler levelUpHandler;

    private LevelDirectUpdateProcessor processor;

    @BeforeEach
    void setUp() {
        badgeCategoryRepository = mock(BadgeCategoryRepository.class);
        badgeRepository = mock(BadgeRepository.class);
        userBadgeRepository = mock(UserBadgeRepository.class);
        levelValueUpdateHandler = mock(ValueUpdateHandler.class);
        levelUpHandler = mock(LevelUpHandler.class);

        processor = new LevelDirectUpdateProcessor(
                badgeCategoryRepository,
                badgeRepository,
                userBadgeRepository,
                levelValueUpdateHandler,
                levelUpHandler
        );
    }

    @DisplayName("최초 UserBadge가 없으면 초기 UserBadge를 생성한다")
    @Test
    void process_초기_UserBadge_없으면_생성한다() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        BadgeCategory badgeCategory = mock(BadgeCategory.class);
        when(badgeCategory.getBadgeStrategy()).thenReturn(BadgeStrategyType.LEVEL_ACCUMULATIVE);
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.of(badgeCategory));

        when(userBadgeRepository.findUserLevelBadge(userId, badgeCategoryId)).thenReturn(Optional.empty());

        Badge badge = mock(Badge.class);
        when(badgeRepository.findInitLevelBadge(badgeCategoryId)).thenReturn(Optional.of(badge));

        // when
        processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE);

        // then
        ArgumentCaptor<UserBadge> captor = ArgumentCaptor.forClass(UserBadge.class);
        verify(userBadgeRepository).save(captor.capture());

        UserBadge saved = captor.getValue();
        assertThat(saved.getUserId()).isEqualTo(userId);
        assertThat(saved.getBadge()).isEqualTo(badge);
        assertThat(saved.getPostStatus()).isEqualTo(PostStatusType.VISIBLE);

        verify(levelUpHandler, never()).handle(any(), any());
    }

    @DisplayName("기존 UserBadge가 있으면 valueUpdateHandler true 시 levelUpHandler 호출한다")
    @Test
    void process_UserBadge_있으면_valueUpdateHandler_호출후_levelUpHandler_호출된다() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        BadgeCategory badgeCategory = mock(BadgeCategory.class);
        when(badgeCategory.getBadgeStrategy()).thenReturn(BadgeStrategyType.LEVEL_ACCUMULATIVE);
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.of(badgeCategory));

        UserBadge existing = mock(UserBadge.class);
        when(userBadgeRepository.findUserLevelBadge(userId, badgeCategoryId)).thenReturn(Optional.of(existing));

        when(levelValueUpdateHandler.handle(existing, BadgeStrategyType.LEVEL_ACCUMULATIVE)).thenReturn(true);

        // when
        processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE);

        // then
        verify(levelValueUpdateHandler).handle(existing, BadgeStrategyType.LEVEL_ACCUMULATIVE);
        verify(levelUpHandler).handle(existing, BadgeStrategyType.LEVEL_ACCUMULATIVE);
    }

    @DisplayName("BadgeCategory가 비활성화 상태이면 아무것도 수행하지 않는다")
    @Test
    void process_비활성화된_카테고리이면_아무것도하지않는다() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(false);

        // when
        processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE);

        // then
        verifyNoInteractions(userBadgeRepository, badgeRepository, levelValueUpdateHandler, levelUpHandler);
    }

    @DisplayName("LEVEL 타입에서 valueUpdateHandler가 false면 levelUpHandler를 호출하지 않는다")
    @Test
    void process_LEVEL_타입은_valueUpdateHandler가_false면_levelUpHandler_호출안됨() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        BadgeCategory badgeCategory = mock(BadgeCategory.class);
        when(badgeCategory.getBadgeStrategy()).thenReturn(BadgeStrategyType.LEVEL);
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.of(badgeCategory));

        UserBadge existingBadge = mock(UserBadge.class);
        when(userBadgeRepository.findUserLevelBadge(userId, badgeCategoryId)).thenReturn(Optional.of(existingBadge));

        when(levelValueUpdateHandler.handle(existingBadge, BadgeStrategyType.LEVEL)).thenReturn(false);

        // when
        processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE);

        // then
        verify(levelValueUpdateHandler).handle(existingBadge, BadgeStrategyType.LEVEL);
        verify(levelUpHandler, never()).handle(any(), any());
    }

    @DisplayName("LEVEL_ACCUMULATIVE 타입에서 valueUpdateHandler true면 levelUpHandler를 호출한다")
    @Test
    void process_LEVEL_ACCUMULATIVE_타입은_valueUpdateHandler가_true면_levelUpHandler_호출됨() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        BadgeCategory badgeCategory = mock(BadgeCategory.class);
        when(badgeCategory.getBadgeStrategy()).thenReturn(BadgeStrategyType.LEVEL_ACCUMULATIVE);
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.of(badgeCategory));

        UserBadge existingBadge = mock(UserBadge.class);
        when(userBadgeRepository.findUserLevelBadge(userId, badgeCategoryId)).thenReturn(Optional.of(existingBadge));

        when(levelValueUpdateHandler.handle(existingBadge, BadgeStrategyType.LEVEL_ACCUMULATIVE)).thenReturn(true);

        // when
        processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE);

        // then
        verify(levelValueUpdateHandler).handle(existingBadge, BadgeStrategyType.LEVEL_ACCUMULATIVE);
        verify(levelUpHandler).handle(existingBadge, BadgeStrategyType.LEVEL_ACCUMULATIVE);
    }

    @DisplayName("LEVEL_ACCUMULATIVE에서 accumulativeValue 증가 검증")
    @Test
    void process_LEVEL_ACCUMULATIVE_실제_UserBadge로_accumulativeValue_검증() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        BadgeCategory badgeCategory = mock(BadgeCategory.class);
        when(badgeCategory.getBadgeStrategy()).thenReturn(BadgeStrategyType.LEVEL_ACCUMULATIVE);
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.of(badgeCategory));

        UserBadge userBadge = UserBadge.builder()
                .userId(userId)
                .accumulativeValue(5)
                .postStatus(PostStatusType.VISIBLE)
                .build();

        when(userBadgeRepository.findUserLevelBadge(userId, badgeCategoryId)).thenReturn(Optional.of(userBadge));

        when(levelValueUpdateHandler.handle(userBadge, BadgeStrategyType.LEVEL_ACCUMULATIVE)).thenAnswer(invocation -> {
            userBadge.updateAccumulativeValueByOne();
            return true;
        });

        // when
        processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE);

        // then
        assertThat(userBadge.getAccumulativeValue()).isEqualTo(6);
        verify(levelUpHandler).handle(userBadge, BadgeStrategyType.LEVEL_ACCUMULATIVE);
    }

    @DisplayName("카테고리가 존재하지 않으면 CustomException(_NOT_FOUND_BADGE_CATEGORY) 발생")
    @Test
    void getBadgeStrategyType_카테고리없으면_CustomException발생() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() ->
                processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE)
        )
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUND_BADGE_CATEGORY.getMessage());
    }

    @DisplayName("초기 Badge가 존재하지 않으면 CustomException(_NOT_FOUND_BADGE) 발생")
    @Test
    void getInitBadge_배지없으면_CustomException발생() {
        // given
        Long userId = 1L;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        BadgeCategory badgeCategory = mock(BadgeCategory.class);
        when(badgeCategory.getBadgeStrategy()).thenReturn(BadgeStrategyType.LEVEL_ACCUMULATIVE);
        when(badgeCategoryRepository.findById(badgeCategoryId)).thenReturn(Optional.of(badgeCategory));

        when(userBadgeRepository.findUserLevelBadge(userId, badgeCategoryId)).thenReturn(Optional.empty());
        when(badgeRepository.findInitLevelBadge(badgeCategoryId)).thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() ->
                processor.process(userId, LevelBadgeCategoryType.RAMEN_REVIEW_CATEGORY_BADGE)
        )
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUND_BADGE.getMessage());
    }

}

