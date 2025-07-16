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
    @Mock private PageUtil pageUtil;

    @InjectMocks private SubscribeServiceImpl subscribeService;

    @Test
    void 구독_존재하면_취소한다() {
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        when(userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId))
                .thenReturn(true);
        when(userService.findExistingUserByUserId(targetUserId))
                .thenReturn(Optional.of(User.builder().id(targetUserId).build()));

        subscribeService.handleSubscribe(targetUserId, requestUserId);

        verify(userSubscriptionRepository).deleteByFollowerIdAndFolloweeId(requestUserId, targetUserId);
        verify(userSubscriptionRepository, never()).save(any());
    }

    @Test
    void 구독_존재하지_않으면_추가한다() {
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        User targetUser = User.builder().id(targetUserId).build();
        User requestUser = User.builder().id(requestUserId).build();

        when(userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId))
                .thenReturn(false);
        when(userService.findExistingUserByUserId(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userService.findExistingUserByUserId(requestUserId)).thenReturn(Optional.of(requestUser));

        subscribeService.handleSubscribe(targetUserId, requestUserId);

        ArgumentCaptor<UserSubscription> captor = ArgumentCaptor.forClass(UserSubscription.class);
        verify(userSubscriptionRepository).save(captor.capture());

        UserSubscription saved = captor.getValue();
        assertThat(saved.getFollower().getId()).isEqualTo(requestUser.getId());
        assertThat(saved.getFollowee().getId()).isEqualTo(targetUser.getId());
    }

    @Test
    void 구독추가시_타겟유저가_없으면_예외() {
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        when(userService.findExistingUserByUserId(targetUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subscribeService.handleSubscribe(targetUserId, requestUserId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(SubscriptionErrorStatus._FAIL_SUBSCRIPTION.getMessage());
    }

    @Test
    void 구독추가시_요청유저가_없으면_예외() {
        Long targetUserId = 1L;
        Long requestUserId = 2L;

        User targetUser = User.builder().id(targetUserId).build();

        when(userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId))
                .thenReturn(false);
        when(userService.findExistingUserByUserId(targetUserId)).thenReturn(Optional.of(targetUser));
        when(userService.findExistingUserByUserId(requestUserId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> subscribeService.handleSubscribe(targetUserId, requestUserId))
                .isInstanceOf(CustomException.class)
                .hasMessageContaining(SubscriptionErrorStatus._FAIL_SUBSCRIPTION.getMessage());
    }

}