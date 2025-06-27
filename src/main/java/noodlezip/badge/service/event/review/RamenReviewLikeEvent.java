package noodlezip.badge.service.event.review;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.dto.EmptyInfoDto;
import noodlezip.badge.service.event.BadgeEventReader;
import noodlezip.badge.service.process.LevelDirectUpdateProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RamenReviewLikeEvent implements BadgeEventReader<EmptyInfoDto> {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Override
    public void read(Long userId, EmptyInfoDto extraOption) {
        processAllCommentPostCount(userId);
    }

    public void processAllCommentPostCount(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.REVIEW_GET_LIKE_COUNT_BADGE);
    }

}
