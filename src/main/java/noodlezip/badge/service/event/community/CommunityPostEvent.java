package noodlezip.badge.service.event.community;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.dto.EmptyInfoDto;
import noodlezip.badge.service.category.AllCommunityPostCountBadge;
import noodlezip.badge.service.event.BadgeEventReader;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommunityPostEvent implements BadgeEventReader<EmptyInfoDto> {

    private final AllCommunityPostCountBadge allCommunityPostCountBadge;

    @Override
    public void read(Long userId, EmptyInfoDto extraOption) {
        allCommunityPostCountBadge.process(userId);
    }


}
