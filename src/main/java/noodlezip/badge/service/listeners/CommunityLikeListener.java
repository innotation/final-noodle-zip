package noodlezip.badge.service.listeners;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.UserEventType;
import noodlezip.badge.events.BasicBadgeEvent;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunityLikeListener {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Async
    @TransactionalEventListener
    public void processCommunityLikeCount(BasicBadgeEvent event) {
        if (event.getEventType() == UserEventType.COMMUNITY_LIKE) {

            try {
                directUpdateProcessor.process(
                        event.getUserId(), LevelBadgeCategoryType.COMMUNITY_GET_LIKE_COUNT_BADGE);
            } catch (Exception e) {
                log.error("[BadgeFail] userId={} event={} badgeType={} reason={}",
                        event.getUserId(),
                        UserEventType.COMMUNITY_LIKE.name(),
                        LevelBadgeCategoryType.COMMUNITY_GET_LIKE_COUNT_BADGE.name(),
                        e.getMessage(), e
                );
            }
        }
    }

    /**
     *     private void publishBadgeEvent(Optional<Like> existingLike, boolean isLiked, Board board) {
     *         if(existingLike.isEmpty() && isLiked) {
     *             if("community".equals(board.getCommunityType())) {
     *                 eventPublisher.publishEvent(new BasicBadgeEvent(
     *                         board.getUser().getId(),
     *                         UserEventType.COMMUNITY_LIKE
     *                 ));
     *             }
     *             if("review".equals(board.getCommunityType())) {
     *                 eventPublisher.publishEvent(new BasicBadgeEvent(
     *                         board.getUser().getId(),
     *                         UserEventType.RAMEN_REVIEW_LIKE
     *                 ));
     *             }
     *         }
     *     }
     */

}
