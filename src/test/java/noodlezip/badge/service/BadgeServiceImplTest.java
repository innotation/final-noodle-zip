package noodlezip.badge.service;

import noodlezip.badge.exception.BadgeErrorStatus;
import noodlezip.badge.repository.BadgeRepository;
import noodlezip.common.exception.CustomException;
import noodlezip.mypage.dto.response.LevelBadgeDetailResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BadgeServiceImplTest {

    @InjectMocks
    private BadgeServiceImpl badgeService;

    @Mock
    private BadgeRepository badgeRepository;

    @Test
    void getNoOptionBadgeDetails_정상() {
        // given
        long userId = 1L;
        long badgeCategoryId = 10L;
        List<LevelBadgeDetailResponse> fakeResponse = List.of(
                LevelBadgeDetailResponse.builder()
                        .badgeId(100L)
                        .imageUrl("test.png")
                        .build()
        );

        when(badgeRepository.findNoOptionBadgeDetails(userId, badgeCategoryId))
                .thenReturn(fakeResponse);

        // when
        List<LevelBadgeDetailResponse> result = badgeService.getNoOptionBadgeDetails(userId, badgeCategoryId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBadgeId()).isEqualTo(100L);
    }

    @Test
    void getNoOptionBadgeDetails_없으면_예외() {
        // given
        long userId = 1L;
        long badgeCategoryId = 10L;
        when(badgeRepository.findNoOptionBadgeDetails(userId, badgeCategoryId)).thenReturn(List.of());

        // when & then
        assertThatThrownBy(() ->
                badgeService.getNoOptionBadgeDetails(userId, badgeCategoryId)
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUNT_USER_BADGE.getMessage());
    }

    @Test
    void getOptionBadgeDetails_정상() {
        // given
        long userId = 1L;
        long badgeId = 20L;
        long badgeCategoryId = 30L;
        List<LevelBadgeDetailResponse> fakeResponse = List.of(
                LevelBadgeDetailResponse.builder()
                        .badgeId(200L)
                        .imageUrl("option.png")
                        .build()
        );

        when(badgeRepository.findOptionBadgeDetails(userId, badgeId, badgeCategoryId))
                .thenReturn(fakeResponse);

        // when
        List<LevelBadgeDetailResponse> result = badgeService.getOptionBadgeDetails(userId, badgeId, badgeCategoryId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBadgeId()).isEqualTo(200L);
    }

    @Test
    void getOptionBadgeDetails_없으면_예외() {
        // given
        long userId = 1L;
        long badgeId = 20L;
        long badgeCategoryId = 30L;
        when(badgeRepository.findOptionBadgeDetails(userId, badgeId, badgeCategoryId)).thenReturn(null);

        // when & then
        assertThatThrownBy(() ->
                badgeService.getOptionBadgeDetails(userId, badgeId, badgeCategoryId)
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUNT_USER_BADGE.getMessage());
    }

}
