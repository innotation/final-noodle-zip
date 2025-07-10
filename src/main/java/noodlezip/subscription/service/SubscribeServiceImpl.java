package noodlezip.subscription.service;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import noodlezip.common.exception.CustomException;
import noodlezip.common.util.PageUtil;
import noodlezip.subscription.dto.response.FolloweeResponse;
import noodlezip.subscription.dto.response.FollowerPageResponse;
import noodlezip.subscription.dto.response.FollowerResponse;
import noodlezip.subscription.dto.response.FollowingPageResponse;
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

    private final EntityManager entityManager;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserRepository userRepository;
    private final PageUtil pageUtil;

    @Override
    @Transactional(readOnly = true)
    public FollowerPageResponse getFollowerListWithPaging(Long targetUserId, Long requestUserId, int page) {
        Pageable pageable = SubscriptionPagePolicy.getPageable(page);
        Page<FollowerResponse> followerList =
                userSubscriptionRepository.findFollowerList(targetUserId, requestUserId, pageable);

        Map<String, Object> pageInfo = pageUtil.getPageInfo(followerList, SubscriptionPagePolicy.PAGE_PER_BLOCK);
        FollowerPageResponse response = FollowerPageResponse.of(pageInfo);
        response.setFollowerList(followerList.getContent());

        return response;
    }

    @Override
    @Transactional(readOnly = true)
    public FollowingPageResponse getFollowingListWithPaging(Long targetUserId, Long requestUserId, int page) {
        Pageable pageable = SubscriptionPagePolicy.getPageable(page);
        Page<FolloweeResponse> followeeList =
                userSubscriptionRepository.findFolloweeList(targetUserId, requestUserId, pageable);

        Map<String, Object> pageInfo = pageUtil.getPageInfo(followeeList, SubscriptionPagePolicy.PAGE_PER_BLOCK);
        FollowingPageResponse response = FollowingPageResponse.of(pageInfo);
        response.setFollowingList(followeeList.getContent());

        return response;
    }

    @Override
    @Transactional
    public void handleSubscribe(Long targetUserId, Long requestUserId) {
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
//        User targetUser = userRepository.findById(targetUserId)
//                .orElseThrow(() -> new CustomException(SubscriptionErrorStatus._FAIL_SUBSCRIPTION));
//        User requestUser = userRepository.findById(requestUserId)
//                .orElseThrow(() -> new CustomException(SubscriptionErrorStatus._FAIL_SUBSCRIPTION));

        User targetUser = User.builder().id(targetUserId).build();
        User requestUser = User.builder().id(requestUserId).build();


        UserSubscription userSubscription = UserSubscription.builder()
                .follower(requestUser)
                .followee(targetUser)
                .build();

        userSubscriptionRepository.save(userSubscription);
    }

}
