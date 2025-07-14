package noodlezip.badge.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.badge.constants.UserEventType;

@AllArgsConstructor
@Getter
public class BasicBadgeEvent {

    private Long userId;
    private UserEventType eventType;

}
