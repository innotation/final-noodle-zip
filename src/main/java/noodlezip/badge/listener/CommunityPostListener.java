package noodlezip.badge.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.UserEventType;
import noodlezip.badge.event.BasicBadgeEvent;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunityPostListener {

    private final LevelDirectUpdateProcessor directUpdateProcessor;


    @Async
    @TransactionalEventListener
    public void processAllCommunityCount(final BasicBadgeEvent event) {
        if (event.getEventType() == UserEventType.COMMUNITY_POST) {

            try {
                directUpdateProcessor.process(
                        event.getUserId(), LevelBadgeCategoryType.ALL_COMMUNITY_POST_COUNT_BADGE);

            } catch (Exception e) {
                log.error("[BadgeFail] userId={} event={} badgeType={} reason={}",
                        event.getUserId(),
                        UserEventType.COMMUNITY_POST.name(),
                        LevelBadgeCategoryType.ALL_COMMUNITY_POST_COUNT_BADGE.name(),
                        e.getMessage(), e
                );
            }
        }
    }

}
