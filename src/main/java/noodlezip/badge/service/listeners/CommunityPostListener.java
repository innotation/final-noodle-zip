package noodlezip.badge.service.listeners;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.constants.UserEventType;
import noodlezip.badge.events.BasicBadgeEvent;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionalEventListener;

@RequiredArgsConstructor
@Service
public class CommunityPostListener {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Async
    @TransactionalEventListener
    public void processAllCommunityCount(BasicBadgeEvent event) {
        if (event.getEventType() == UserEventType.COMMUNITY_POST) {

            directUpdateProcessor.process(
                    event.getUserId(), LevelBadgeCategoryType.ALL_COMMUNITY_POST_COUNT_BADGE);
        }
    }

}
