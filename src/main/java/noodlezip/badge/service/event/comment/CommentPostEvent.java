package noodlezip.badge.service.event.comment;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.dto.request.EmptyInfoRequest;
import noodlezip.badge.service.event.BadgeEventReader;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentPostEvent implements BadgeEventReader<EmptyInfoRequest> {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Override
    public void read(Long userId, EmptyInfoRequest extraOption) {
        processAllCommentPostCount(userId);
    }

    public void processAllCommentPostCount(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.ALL_COMMENT_POST_COUNT_BADGE);
    }

}
