package noodlezip.badge.service.category;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.service.process.LevelStrategyDirectUpdateProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class AllCommunityPostCountBadge {

    private final LevelStrategyDirectUpdateProcessor directUpdateProcessor;

    public void process(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.ALL_COMMENT_POST_COUNT_BADGE);
    }

}
