package noodlezip.badge.service.event.comment;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.dto.EmptyInfoDto;
import noodlezip.badge.service.event.BadgeEventReader;
import noodlezip.badge.service.process.LevelStrategyDirectUpdateProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentPostEvent implements BadgeEventReader<EmptyInfoDto> {

    private final LevelStrategyDirectUpdateProcessor directUpdateProcessor;

    @Override
    public void read(Long userId, EmptyInfoDto extraOption) {
        processAllCommentPostCount(userId);
    }

    public void processAllCommentPostCount(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.ALL_COMMENT_POST_COUNT_BADGE);
    }


}
