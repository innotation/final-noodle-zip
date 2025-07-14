package noodlezip.badge.service;

import noodlezip.badge.constants.BadgeStrategyType;
import noodlezip.badge.constants.Region;
import noodlezip.badge.dto.response.*;
import noodlezip.badge.entity.BadgeCategory;
import noodlezip.badge.status.BadgeErrorStatus;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.badge.dto.UserNoOptionBadgeDto;
import noodlezip.badge.dto.UserOptionBadgeDto;
import noodlezip.user.entity.User;
import noodlezip.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MyBadgeServiceImplTest {

    @InjectMocks
    private MyBadgeServiceImpl myBadgeService;

    @Mock
    private UserService userService;
    @Mock
    private BadgeCategoryService badgeCategoryService;
    @Mock
    private BadgeService badgeService;
    @Mock
    private UserBadgeService userBadgeService;

    @Test
    void getUserBadgeListByGroup_성공() {
        // given
        Long userId = 1L;

        when(userService.findExistingUserByUserId(userId))
                .thenReturn(Optional.of(User.builder().id(userId).build()));

        List<BadgeGroupResponse> badgeGroupList = new ArrayList<>(List.of(
                BadgeGroupResponse.builder().id(1L).badgeGroupName("좋아요").build(),
                BadgeGroupResponse.builder().id(2L).badgeGroupName("소통").build()
        ));

        List<UserNoOptionBadgeDto> notOptionBadgeList = new ArrayList<>(List.of(
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
        ));

        List<UserOptionBadgeDto> optionBadgeList = new ArrayList<>(List.of(
                UserOptionBadgeDto.builder()
                        .userBadgeId(200L)
                        .badgeId(20L)
                        .badgeCategoryId(201L)
                        .badgeGroupId(2L)
                        .storeSidoLegalCode(null)
                        .ramenCategoryName("돈코츠")
                        .badgeName("라멘 LV1")
                        .accumulativeValue(3)
                        .badgeImageUrl("ramen_lv1.png")
                        .build()
        ));

        when(userBadgeService.getBadgeGroupList()).thenReturn(badgeGroupList);
        when(userBadgeService.getNoOptionUserBadgeList(userId)).thenReturn(notOptionBadgeList);
        when(userBadgeService.getOptionUserBadgeList(userId)).thenReturn(optionBadgeList);

        // when
        List<MyBadgeBadgeResponse> result = myBadgeService.getUserBadgeListByGroup(userId);

        // then
        assertThat(result).hasSize(2);
        MyBadgeBadgeResponse likeGroup = result.get(0);
        assertThat(likeGroup.getBadgeGroup().getBadgeGroupName()).isEqualTo("좋아요");
        assertThat(likeGroup.getUserBadgeList()).hasSize(1);
        assertThat(likeGroup.getUserBadgeList().get(0).getBadgeTitleName()).isEqualTo("좋아요 횟수");

        MyBadgeBadgeResponse ramenGroup = result.get(1);
        assertThat(ramenGroup.getBadgeGroup().getBadgeGroupName()).isEqualTo("소통");
        assertThat(ramenGroup.getUserBadgeList()).hasSize(1);
        assertThat(ramenGroup.getUserBadgeList().get(0).getBadgeTitleName()).isEqualTo("돈코츠");
    }

    @Test
    void getBadgeDetailList_성공() {
        // given
        Long userId = 1L;
        Long badgeId = 10L;
        Long badgeCategoryId = 2L;

        BadgeCategory badgeCategory = BadgeCategory.builder()
                .id(badgeCategoryId)
                .badgeDescription("좋아요 모음 뱃지")
                .badgeStrategy(BadgeStrategyType.LEVEL)
                .isActive(true)
                .build();

        when(badgeCategoryService.getBadgeCategoryById(badgeCategoryId)).thenReturn(badgeCategory);

        List<LevelBadgeDetailResponse> levelBadgeDetailList = new ArrayList<>(List.of(
                LevelBadgeDetailResponse.builder()
                        .badgeId(badgeId)
                        .imageUrl("main.png")
                        .obtainedDate(LocalDateTime.now())
                        .build(),
                LevelBadgeDetailResponse.builder()
                        .badgeId(11L)
                        .imageUrl("next.png")
                        .obtainedDate(null)
                        .build()
        ));

        when(badgeService.getNoOptionBadgeDetails(userId, badgeCategoryId)).thenReturn(levelBadgeDetailList);

        // when
        BadgeDetailResponse result = myBadgeService.getBadgeDetailList(userId, badgeId, badgeCategoryId);

        // then
        assertThat(result.getMainTitle()).isEqualTo("좋아요 모음 뱃지");
        assertThat(result.getMainImageUrl()).isEqualTo("main.png");
        assertThat(result.getLevelBadgeDetailList()).hasSize(2);
    }

    @Test
    void getBadgeDetailList_없는카테고리_예외발생() {
        // given
        Long userId = 1L;
        Long badgeId = 10L;
        Long badgeCategoryId = 1000L; // 없는 ID

        BadgeCategory badgeCategory = BadgeCategory.builder()
                .id(badgeCategoryId)
                .badgeDescription("잘못된 카테고리")
                .badgeStrategy(BadgeStrategyType.LEVEL)
                .isActive(true)
                .build();

        when(badgeCategoryService.getBadgeCategoryById(badgeCategoryId)).thenReturn(badgeCategory);

        // when & then
        assertThatThrownBy(() ->
                myBadgeService.getBadgeDetailList(userId, badgeId, badgeCategoryId)
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(BadgeErrorStatus._NOT_FOUND_BADGE_CATEGORY.getMessage());
    }

    @Test
    void getUserBadgeListByGroup_옵션형_라멘카테고리만() {
        // given
        Long userId = 1L;

        when(userService.findExistingUserByUserId(userId))
                .thenReturn(Optional.of(User.builder().id(userId).build()));

        List<BadgeGroupResponse> badgeGroupList = List.of(
                BadgeGroupResponse.builder().id(2L).badgeGroupName("소통").build()
        );

        List<UserOptionBadgeDto> optionBadgeList = new ArrayList<>(List.of(
                UserOptionBadgeDto.builder()
                        .userBadgeId(200L)
                        .badgeId(20L)
                        .badgeCategoryId(201L)
                        .badgeGroupId(2L)
                        .storeSidoLegalCode(null)
                        .ramenCategoryName("돈코츠")
                        .badgeName("라멘 LV1")
                        .accumulativeValue(3)
                        .badgeImageUrl("ramen_lv1.png")
                        .build()
        ));

        when(userBadgeService.getBadgeGroupList()).thenReturn(badgeGroupList);
        when(userBadgeService.getNoOptionUserBadgeList(userId)).thenReturn(new ArrayList<>());
        when(userBadgeService.getOptionUserBadgeList(userId)).thenReturn(optionBadgeList);

        // when
        List<MyBadgeBadgeResponse> result = myBadgeService.getUserBadgeListByGroup(userId);

        // then
        assertThat(result).hasSize(1);
        UserBadgeResponse badge = result.get(0).getUserBadgeList().get(0);
        assertThat(badge.getBadgeTitleName()).isEqualTo("돈코츠");
    }

    @Test
    void getUserBadgeListByGroup_옵션형_지역만() {
        // given
        Long userId = 1L;

        when(userService.findExistingUserByUserId(userId))
                .thenReturn(Optional.of(User.builder().id(userId).build()));

        List<BadgeGroupResponse> badgeGroupList = List.of(
                BadgeGroupResponse.builder().id(2L).badgeGroupName("소통").build()
        );

        List<UserOptionBadgeDto> optionBadgeList = new ArrayList<>(List.of(
                UserOptionBadgeDto.builder()
                        .userBadgeId(201L)
                        .badgeId(21L)
                        .badgeCategoryId(202L)
                        .badgeGroupId(2L)
                        .storeSidoLegalCode(11)
                        .ramenCategoryName(null)
                        .badgeName("지역 LV1")
                        .accumulativeValue(4)
                        .badgeImageUrl("region_lv1.png")
                        .build()
        ));

        when(userBadgeService.getBadgeGroupList()).thenReturn(badgeGroupList);
        when(userBadgeService.getNoOptionUserBadgeList(userId)).thenReturn(new ArrayList<>());
        when(userBadgeService.getOptionUserBadgeList(userId)).thenReturn(optionBadgeList);

        // when
        List<MyBadgeBadgeResponse> result = myBadgeService.getUserBadgeListByGroup(userId);

        // then
        assertThat(result).hasSize(1);
        UserBadgeResponse badge = result.get(0).getUserBadgeList().get(0);
        assertThat(badge.getBadgeTitleName()).isEqualTo(Region.SEOUL.getName());
    }

    @Test
    void getUserBadgeListByGroup_옵션형_둘다존재하면_예외() {
        // given
        Long userId = 1L;

        when(userService.findExistingUserByUserId(userId))
                .thenReturn(Optional.of(User.builder().id(userId).build()));

        List<BadgeGroupResponse> badgeGroupList = List.of(
                BadgeGroupResponse.builder().id(2L).badgeGroupName("소통").build()
        );

        List<UserOptionBadgeDto> optionBadgeList = new ArrayList<>(List.of(
                UserOptionBadgeDto.builder()
                        .userBadgeId(202L)
                        .badgeId(22L)
                        .badgeCategoryId(203L)
                        .badgeGroupId(2L)
                        .storeSidoLegalCode(11)
                        .ramenCategoryName("쇼유")
                        .badgeName("충돌 LV1")
                        .accumulativeValue(2)
                        .badgeImageUrl("conflict_lv1.png")
                        .build()
        ));
        when(userBadgeService.getBadgeGroupList()).thenReturn(badgeGroupList);
        when(userBadgeService.getNoOptionUserBadgeList(userId)).thenReturn(new ArrayList<>());
        when(userBadgeService.getOptionUserBadgeList(userId)).thenReturn(optionBadgeList);

        // when & then
        assertThatThrownBy(() ->
                myBadgeService.getUserBadgeListByGroup(userId)
        ).isInstanceOf(CustomException.class)
                .hasMessageContaining(ErrorStatus._INTERNAL_SERVER_ERROR.getMessage());
    }

}