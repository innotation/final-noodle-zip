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
public class RamenReviewLikeListener {

    private final LevelDirectUpdateProcessor directUpdateProcessor;


    @Async
    @TransactionalEventListener
    public void processReviewGetLikeCount(BasicBadgeEvent event) {
        if (event.getEventType() == UserEventType.RAMEN_REVIEW_LIKE) {

            try {
                directUpdateProcessor.process(
                        event.getUserId(), LevelBadgeCategoryType.REVIEW_GET_LIKE_COUNT_BADGE);

            } catch (Exception e) {
                log.error("[BadgeFail] userId={} event={} badgeType={} reason={}",
                        event.getUserId(),
                        UserEventType.RAMEN_REVIEW_LIKE.name(),
                        LevelBadgeCategoryType.REVIEW_GET_LIKE_COUNT_BADGE.name(),
                        e.getMessage(), e
                );
            }
        }
    }

}
