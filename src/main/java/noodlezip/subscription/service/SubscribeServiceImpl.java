package noodlezip.subscription.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.subscription.constants.SubscriptionType;
import noodlezip.subscription.dto.response.SubscriberResponse;
import noodlezip.subscription.dto.response.SubscriptionPageResponse;
import noodlezip.subscription.entity.UserSubscription;
import noodlezip.subscription.repository.UserSubscriptionRepository;
import noodlezip.subscription.status.SubscriptionErrorStatus;
import noodlezip.subscription.util.SubscriptionPagePolicy;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class SubscribeServiceImpl implements SubscribeService {

    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;
    private final PageUtil pageUtil;


    @Override
    @Transactional(readOnly = true)
    public SubscriptionPageResponse getFollowerListWithPaging(Long targetUserId, Long requestUserId, int page) {
        Pageable pageable = SubscriptionPagePolicy.getPageable(page);
        Page<SubscriberResponse> followerList = getFollowerListByLoginStatus(targetUserId, requestUserId, pageable);

        Map<String, Object> pageInfo = pageUtil.getPageInfo(followerList, SubscriptionPagePolicy.PAGE_PER_BLOCK);
        SubscriptionPageResponse response = SubscriptionPageResponse.pageOf(pageInfo);
        response.setRequestUserId(requestUserId);
        response.setTargetUserId(targetUserId);
        response.setSubscriptionList(followerList.getContent());
        response.setSubscriptionType(SubscriptionType.FOLLOWER);

        return response;
    }

    private Page<SubscriberResponse> getFollowerListByLoginStatus(Long targetUserId, Long requestUserId, Pageable pageable) {
        if (isLoginUser(requestUserId)) {
            return userSubscriptionRepository.findFollowerList(targetUserId, requestUserId, pageable);
        }
        return userSubscriptionRepository.findFollowerListWithoutLoginUser(targetUserId, pageable);
    }


    @Override
    @Transactional(readOnly = true)
    public SubscriptionPageResponse getFollowingListWithPaging(Long targetUserId, Long requestUserId, int page) {
        Pageable pageable = SubscriptionPagePolicy.getPageable(page);
        Page<SubscriberResponse> followeeList = getFollowingListByLoginStatus(targetUserId, requestUserId, pageable);

        Map<String, Object> pageInfo = pageUtil.getPageInfo(followeeList, SubscriptionPagePolicy.PAGE_PER_BLOCK);
        SubscriptionPageResponse response = SubscriptionPageResponse.pageOf(pageInfo);
        response.setRequestUserId(requestUserId);
        response.setTargetUserId(targetUserId);
        response.setSubscriptionList(followeeList.getContent());
        response.setSubscriptionType(SubscriptionType.FOLLOWING);

        return response;
    }

    private  Page<SubscriberResponse> getFollowingListByLoginStatus(Long targetUserId, Long requestUserId, Pageable pageable) {
        if(isLoginUser(requestUserId)) {
            return userSubscriptionRepository.findFolloweeList(targetUserId, requestUserId, pageable);
        }
        return userSubscriptionRepository.findFolloweeListWithoutLoginUser(targetUserId, pageable);
    }

    private boolean isLoginUser(Long requestUserId) {
        return requestUserId != null && userRepository.existsById(requestUserId);
    }


    @Override
    @Transactional
    public void handleSubscribe(Long targetUserId, Long requestUserId) {
        if (targetUserId == null || requestUserId == null) {
            throw new CustomException(SubscriptionErrorStatus._FAIL_SUBSCRIPTION);
        }
        if (targetUserId.equals(requestUserId)) {
            throw new CustomException(SubscriptionErrorStatus._FAIL_SUBSCRIPTION);
        }

        boolean isExists = userSubscriptionRepository.existsByFollowerIdAndFolloweeId(requestUserId, targetUserId);
        if (isExists) {
            cancelSubscription(targetUserId, requestUserId);
        } else {
            addSubscription(targetUserId, requestUserId);
        }
    }

    private void cancelSubscription(Long targetUserId, Long requestUserId) {
        userSubscriptionRepository.deleteByFollowerIdAndFolloweeId(requestUserId, targetUserId);
    }

    private void addSubscription(Long targetUserId, Long requestUserId) {
        User targetUser = userRepository.findById(targetUserId)
                .orElseThrow(() -> new CustomException(SubscriptionErrorStatus._FAIL_SUBSCRIPTION));
        User requestUser = userRepository.findById(requestUserId)
                .orElseThrow(() -> new CustomException(SubscriptionErrorStatus._FAIL_SUBSCRIPTION));

        UserSubscription userSubscription = UserSubscription.builder()
                .follower(requestUser)
                .followee(targetUser)
                .build();

        userSubscriptionRepository.save(userSubscription);
    }

}
