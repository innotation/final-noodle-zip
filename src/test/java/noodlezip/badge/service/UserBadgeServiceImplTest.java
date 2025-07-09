package noodlezip.badge.service;

import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.repository.BadgeGroupRepository;
import noodlezip.badge.repository.UserBadgeRepository;
import noodlezip.badge.dto.UserNoOptionBadgeDto;
import noodlezip.badge.dto.UserOptionBadgeDto;
import noodlezip.badge.dto.response.BadgeGroupResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserBadgeServiceImplTest {

    @InjectMocks
    private UserBadgeServiceImpl userBadgeService;

    @Mock
    private BadgeGroupRepository badgeGroupRepository;

    @Mock
    private UserBadgeRepository userBadgeRepository;

    @Test
    void getBadgeGroupIds_정상() {
        // given
        List<BadgeGroupResponse> groupList = List.of(
                BadgeGroupResponse.builder().id(1L).badgeGroupName("좋아요").build()
        );
        when(badgeGroupRepository.getBadgeGroups()).thenReturn(groupList);

        // when
        List<BadgeGroupResponse> result = userBadgeService.getBadgeGroupList();

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBadgeGroupName()).isEqualTo("좋아요");
    }

    @Test
    void getNoOptionUserBadgeList_정상() {
        // given
        Long userId = 1L;
        List<UserNoOptionBadgeDto> noOptionList = List.of(
                UserNoOptionBadgeDto.builder()
                        .userBadgeId(100L)
                        .badgeId(10L)
                        .badgeCategoryId(101L)
                        .badgeGroupId(1L)
                        .badgeCategoryName("좋아요 횟수")
                        .badgeName("좋아요 LV1")
                        .accumulativeValue(5)
                        .badgeImageUrl("like_lv1.png")
                        .build()
        );

        List<Long> categoryIds = LevelBadgeCategoryType.getNoOptionBadgeCategoryIdList();
        when(userBadgeRepository.findNoOptionBadgeList(userId, categoryIds)).thenReturn(noOptionList);

        // when
        List<UserNoOptionBadgeDto> result = userBadgeService.getNoOptionUserBadgeList(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBadgeName()).isEqualTo("좋아요 LV1");
    }

    @Test
    void getOptionUserBadgeList_정상() {
        // given
        Long userId = 2L;
        List<UserOptionBadgeDto> optionList = List.of(
                UserOptionBadgeDto.builder()
                        .userBadgeId(200L)
                        .badgeId(20L)
                        .badgeCategoryId(202L)
                        .badgeGroupId(2L)
                        .storeSidoLegalCode(11)
                        .ramenCategoryName(null)
                        .badgeName("지역 LV1")
                        .accumulativeValue(2)
                        .badgeImageUrl("region_lv1.png")
                        .build()
        );

        List<Long> categoryIds = LevelBadgeCategoryType.getOptionBadgeCategoryIdList();
        when(userBadgeRepository.findOptionBadgeList(userId, categoryIds)).thenReturn(optionList);

        // when
        List<UserOptionBadgeDto> result = userBadgeService.getOptionUserBadgeList(userId);

        // then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getBadgeName()).isEqualTo("지역 LV1");
    }
}
