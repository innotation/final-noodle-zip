package noodlezip.badge.service.event;

import noodlezip.badge.dto.BadgeExtraOptionDto;

public interface BadgeEventReader<T extends BadgeExtraOptionDto> {

    void read(Long userId, T extraOption);

}
