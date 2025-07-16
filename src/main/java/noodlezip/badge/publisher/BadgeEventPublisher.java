package noodlezip.badge.publisher;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.UserEventType;
import noodlezip.badge.event.BasicBadgeEvent;
import noodlezip.badge.event.RamenReviewBadgeEvent;
import noodlezip.community.dto.CommentReqDto;
import noodlezip.community.dto.MenuReviewDto;
import noodlezip.community.dto.ReviewReqDto;
import noodlezip.community.entity.Board;
import noodlezip.user.entity.User;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class BadgeEventPublisher {

    private final ApplicationEventPublisher eventPublisher;


    public void publishCommentBadgeEvent(CommentReqDto info) {
        eventPublisher.publishEvent(new BasicBadgeEvent(
                info.getUserId(),
                UserEventType.COMMENT_POST
        ));
    }


    public void publishCommunityLikeBadgeEvent(Board board) {
        String communityType = board.getCommunityType();

        if ("community".equals(communityType)) {
            eventPublisher.publishEvent(new BasicBadgeEvent(
                    board.getUser().getId(),
                    UserEventType.COMMUNITY_LIKE
            ));

        } else if ("review".equals(communityType)) {
            eventPublisher.publishEvent(new BasicBadgeEvent(
                    board.getUser().getId(),
                    UserEventType.RAMEN_REVIEW_LIKE
            ));
        }

    }


    public void publishCommunityPostBadgeEvent(User user) {
        eventPublisher.publishEvent(new BasicBadgeEvent(
                user.getId(),
                UserEventType.COMMUNITY_POST
        ));
    }


    public void publishRamenReviewBadgeEvent(User user, ReviewReqDto reviewInfo) {
        Long storeId = reviewInfo.getStoreId();
        List<Long> menuIdList = reviewInfo.getReviews().stream()
                .map(MenuReviewDto::getMenuId)
                .collect(Collectors.toList());

        eventPublisher.publishEvent(new RamenReviewBadgeEvent(
                user.getId(),
                storeId,
                menuIdList
        ));
    }

}
