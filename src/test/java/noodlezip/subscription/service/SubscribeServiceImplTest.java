package noodlezip.subscription.service;


import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.subscription.dto.response.*;
import noodlezip.subscription.entity.UserSubscription;
import noodlezip.subscription.repository.UserSubscriptionRepository;
import noodlezip.subscription.status.SubscriptionErrorStatus;
import noodlezip.subscription.util.SubscriptionPagePolicy;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import noodlezip.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscribeServiceImplTest {

    @Mock private UserSubscriptionRepository userSubscriptionRepository;
    @Mock private UserService userService;
    @Mock private UserRepository userRepository;
    @Mock private PageUtil pageUtil;

    @InjectMocks private SubscribeServiceImpl subscribeService;

    @Test
    void 구독_존재하면_취소한다() {
        // given
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        when(userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId))
                .thenReturn(true);

        // when
        subscribeService.handleSubscribe(targetUserId, requestUserId);

        // then
        verify(userSubscriptionRepository).deleteByFollowerIdAndFolloweeId(requestUserId, targetUserId);
        verify(userSubscriptionRepository, never()).save(any());
    }

    @Test
    void 구독_존재하지_않으면_추가한다() {
        // given
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        User targetUser = User.builder().id(targetUserId).build();
        User requestUser = User.builder().id(requestUserId).build();

        when(userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId))
                .thenReturn(false);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userRepository.findById(requestUserId)).thenReturn(Optional.of(requestUser));

        // when
        subscribeService.handleSubscribe(targetUserId, requestUserId);

        // then
        ArgumentCaptor<UserSubscription> captor = ArgumentCaptor.forClass(UserSubscription.class);
        verify(userSubscriptionRepository).save(captor.capture());

        UserSubscription saved = captor.getValue();
        // equals 깨지지 않게 ID만 비교
        assertThat(saved.getFollower().getId()).isEqualTo(requestUser.getId());
        assertThat(saved.getFollowee().getId()).isEqualTo(targetUser.getId());
    }

    @Test
    void 구독추가시_타겟유저가_없으면_예외() {
        // given
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        when(userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId))
                .thenReturn(false);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> subscribeService.handleSubscribe(targetUserId, requestUserId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(SubscriptionErrorStatus._FAIL_SUBSCRIPTION.getMessage());
    }

    @Test
    void 구독추가시_요청유저가_없으면_예외() {
        // given
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        User targetUser = User.builder().id(targetUserId).build();

        when(userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId))
                .thenReturn(false);
        when(userRepository.findById(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userRepository.findById(requestUserId)).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> subscribeService.handleSubscribe(targetUserId, requestUserId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(SubscriptionErrorStatus._FAIL_SUBSCRIPTION.getMessage());
    }

    @Test
    void 팔로워_페이지_조회_구조_검증() {
        // given
        Long targetUserId = 1L;
        Long requestUserId = 2L;
        int page = 1;

        when(userRepository.existsById(requestUserId)).thenReturn(true);

        SubscriberResponse dto = new SubscriberResponse(10L, 100L, "Alice","Alice", "alice.jpg", true);
        Page<SubscriberResponse> followerPage = new PageImpl<>(List.of(dto), PageRequest.of(0, 30), 20);

        Map<String, Object> dummyPageInfo = Map.of(
                "page", 1,
                "totalPage", 10,
                "beginPage", 1,
                "endPage", 5,
                "isFirst", true,
                "isLast", false
        );

        when(userSubscriptionRepository.findFollowerList(eq(targetUserId), eq(requestUserId), any(Pageable.class)))
                .thenReturn(followerPage);
        doReturn(dummyPageInfo)
                .when(pageUtil)
                .getPageInfo(any(Page.class), eq(SubscriptionPagePolicy.PAGE_PER_BLOCK));

        // when
        SubscriptionPageResponse response = subscribeService.getFollowerListWithPaging(targetUserId, requestUserId, page);

        // then
        assertThat(response.getPage()).isEqualTo(1);
        assertThat(response.getTotalPage()).isEqualTo(10);
        assertThat(response.getBeginPage()).isEqualTo(1);
        assertThat(response.getEndPage()).isEqualTo(5);
        assertThat(response.isFirst()).isTrue();
        assertThat(response.isLast()).isFalse();

        assertThat(response.getSubscriptionList()).hasSize(1);
        assertThat(response.getSubscriptionList()).extracting("name").containsExactly("Alice");

        verify(userSubscriptionRepository).findFollowerList(eq(targetUserId), eq(requestUserId), any(Pageable.class));
        verify(pageUtil).getPageInfo(any(Page.class), eq(SubscriptionPagePolicy.PAGE_PER_BLOCK));
    }
    
}