package noodlezip.badge.service.event.community;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.constants.LevelBadgeCategoryType;
import noodlezip.badge.dto.EmptyInfoDto;
import noodlezip.badge.service.process.level.LevelDirectUpdateProcessor;
import noodlezip.badge.service.event.BadgeEventReader;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommunityPostEvent implements BadgeEventReader<EmptyInfoDto> {

    private final LevelDirectUpdateProcessor directUpdateProcessor;

    @Override
    public void read(Long userId, EmptyInfoDto extraOption) {
        processAllCommunityCount(userId);
    }

    public void processAllCommunityCount(Long userId) {
        directUpdateProcessor.process(userId, LevelBadgeCategoryType.ALL_COMMUNITY_POST_COUNT_BADGE);
    }

}
