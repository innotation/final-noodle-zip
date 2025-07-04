package noodlezip.badge.service.event;

import noodlezip.badge.dto.request.BadgeExtraOptionRequest;

public interface BadgeEventReader<T extends BadgeExtraOptionRequest> {

    void read(Long userId, T extraOption);

}
