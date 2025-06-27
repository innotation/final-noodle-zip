package noodlezip.badge.service.event.community;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.dto.EmptyInfoDto;
import noodlezip.badge.service.event.BadgeEventReader;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommunityLikeEvent implements BadgeEventReader<EmptyInfoDto> {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Override
    public void read(Long userId, EmptyInfoDto extraOption) {
        processCommunityLikeCount(userId);
    }

    public void processCommunityLikeCount(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.COMMUNITY_GET_LIKE_COUNT_BADGE);
    }


}
