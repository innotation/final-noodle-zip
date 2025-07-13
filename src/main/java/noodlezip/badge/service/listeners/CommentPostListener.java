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
public class CommentPostListener {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Async
    @TransactionalEventListener
    public void processAllCommentPostCount(BasicBadgeEvent event) {
        if (event.getEventType() == UserEventType.COMMENT_POST) {

            try {
                directUpdateProcessor.process(
                        event.getUserId(), LevelBadgeCategoryType.ALL_COMMENT_POST_COUNT_BADGE);
            } catch (Exception e) {
                log.error("[BadgeFail] userId={} event={} badgeType={} reason={}",
                        event.getUserId(),
                        UserEventType.COMMENT_POST.name(),
                        LevelBadgeCategoryType.ALL_COMMENT_POST_COUNT_BADGE.name(),
                        e.getMessage(), e
                );
            }
        }
    }

    /**
     *     private void publishBadgeEvent(CommentReqDto info) {
     *         eventPublisher.publishEvent(new BasicBadgeEvent(
     *                 info.getUserId(),
     *                 UserEventType.COMMENT_POST
     *         ));
     *     }
     */

}
