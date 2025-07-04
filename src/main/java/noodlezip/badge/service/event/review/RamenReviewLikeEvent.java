package noodlezip.badge.service.event.review;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.dto.request.EmptyInfoRequest;
import noodlezip.badge.service.event.BadgeEventReader;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class RamenReviewLikeEvent implements BadgeEventReader<EmptyInfoRequest> {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Override
    public void read(Long userId, EmptyInfoRequest extraOption) {
        processReviewGetLikeCount(userId);
    }

    public void processReviewGetLikeCount(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.REVIEW_GET_LIKE_COUNT_BADGE);
    }

}
