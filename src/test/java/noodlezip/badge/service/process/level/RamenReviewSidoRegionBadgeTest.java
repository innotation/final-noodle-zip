package noodlezip.badge.service.process.level;

import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.PostStatusType;
import noodlezip.badge.constants.Region;
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
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class RamenReviewSidoRegionBadgeTest {

    private BadgeCategoryRepository badgeCategoryRepository;
    private BadgeRepository badgeRepository;
    private UserBadgeRepository userBadgeRepository;
    private ValueUpdateHandler levelValueUpdateHandler;
    private LevelUpHandler levelUpHandler;

    private RamenReviewSidoRegionBadge service;

    @BeforeEach
    void setUp() {
        badgeCategoryRepository = mock(BadgeCategoryRepository.class);
        badgeRepository = mock(BadgeRepository.class);
        userBadgeRepository = mock(UserBadgeRepository.class);
        levelValueUpdateHandler = mock(ValueUpdateHandler.class);
        levelUpHandler = mock(LevelUpHandler.class);

        service = new RamenReviewSidoRegionBadge(
                badgeCategoryRepository,
                badgeRepository,
                userBadgeRepository,
                levelValueUpdateHandler,
                levelUpHandler
        );
    }

    @DisplayName("최초 UserBadge 없으면 초기 UserBadge를 생성한다")
    @Test
    void 최초_UserBadge_없으면_초기_생성() {
        // given
        Long userId = 1L;
        Region sidoRegion = Region.SEOUL;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_REGION_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);
        when(userBadgeRepository.findSidoRegionLevelUserBadge(userId, badgeCategoryId, sidoRegion.getCode()))
                .thenReturn(Optional.empty());

        Badge badge = mock(Badge.class);
        when(badgeRepository.findInitSidoRegionLevelBadge(badgeCategoryId, sidoRegion.getCode()))
                .thenReturn(Optional.of(badge));

        // when
        service.process(userId, sidoRegion);

        // then
        ArgumentCaptor<UserBadge> captor = ArgumentCaptor.forClass(UserBadge.class);
        verify(userBadgeRepository).save(captor.capture());

        UserBadge savedBadge = captor.getValue();
        assertThat(savedBadge.getUserId()).isEqualTo(userId);
        assertThat(savedBadge.getBadge()).isEqualTo(badge);
        assertThat(savedBadge.getCurrentValue()).isEqualTo(1);
        assertThat(savedBadge.getAccumulativeValue()).isEqualTo(1);
        assertThat(savedBadge.getPostStatus()).isEqualTo(PostStatusType.VISIBLE);

        verify(levelUpHandler, never()).handle(any(), any());
    }

    @DisplayName("기존 UserBadge가 있으면 valueUpdateHandler true 시 levelUpHandler 호출")
    @Test
    void 기존_UserBadge있으면_valueUpdateHandler_true면_levelUpHandler_호출() {
        // given
        Long userId = 1L;
        Region sidoRegion = Region.BUSAN;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_REGION_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        UserBadge existingBadge = mock(UserBadge.class);
        when(userBadgeRepository.findSidoRegionLevelUserBadge(userId, badgeCategoryId, sidoRegion.getCode()))
                .thenReturn(Optional.of(existingBadge));

        when(levelValueUpdateHandler.updateLevelAccumulativeTypeBadgeValue(existingBadge)).thenReturn(true);

        // when
        service.process(userId, sidoRegion);

        // then
        verify(levelValueUpdateHandler).updateLevelAccumulativeTypeBadgeValue(existingBadge);
        verify(levelUpHandler).handle(existingBadge, BadgeStrategyType.LEVEL_ACCUMULATIVE);
    }

    @DisplayName("기존 UserBadge가 있으면 valueUpdateHandler false 시 levelUpHandler를 호출하지 않음")
    @Test
    void 기존_UserBadge있으면_valueUpdateHandler_false면_levelUpHandler_호출안함() {
        // given
        Long userId = 1L;
        Region sidoRegion = Region.INCHEON;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_REGION_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);

        UserBadge existingBadge = mock(UserBadge.class);
        when(userBadgeRepository.findSidoRegionLevelUserBadge(userId, badgeCategoryId, sidoRegion.getCode()))
                .thenReturn(Optional.of(existingBadge));

        when(levelValueUpdateHandler.updateLevelAccumulativeTypeBadgeValue(existingBadge)).thenReturn(false);

        // when
        service.process(userId, sidoRegion);

        // then
        verify(levelValueUpdateHandler).updateLevelAccumulativeTypeBadgeValue(existingBadge);
        verify(levelUpHandler, never()).handle(any(), any());
    }

    @DisplayName("BadgeCategory가 비활성 상태이면 아무것도 수행하지 않는다")
    @Test
    void 비활성화된_카테고리면_아무것도안함() {
        // given
        Long userId = 1L;
        Region sidoRegion = Region.SEOUL;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_REGION_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(false);

        // when
        service.process(userId, sidoRegion);

        // then
        verifyNoInteractions(userBadgeRepository, badgeRepository, levelValueUpdateHandler, levelUpHandler);
    }

    @DisplayName("초기 Badge가 존재하지 않으면 CustomException(_NOT_FOUND_BADGE) 발생")
    @Test
    void 초기_Badge없으면_CustomException발생() {
        // given
        Long userId = 1L;
        Region sidoRegion = Region.JEJU;
        Long badgeCategoryId = LevelBadgeCategoryType.RAMEN_REVIEW_REGION_BADGE.getDbPk();

        when(badgeCategoryRepository.existsByIdAndIsActiveTrue(badgeCategoryId)).thenReturn(true);
        when(userBadgeRepository.findSidoRegionLevelUserBadge(userId, badgeCategoryId, sidoRegion.getCode()))
                .thenReturn(Optional.empty());
        when(badgeRepository.findInitSidoRegionLevelBadge(badgeCategoryId, sidoRegion.getCode()))
                .thenReturn(Optional.empty());

        // then
        assertThatThrownBy(() ->
                service.process(userId, sidoRegion)
        )
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUND_BADGE.getMessage());
    }

}