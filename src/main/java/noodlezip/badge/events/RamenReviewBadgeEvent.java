package noodlezip.badge.events;

import lombok.AllArgsConstructor;
import lombok.Getter;
import noodlezip.badge.constants.UserEventType;

import java.util.List;

@AllArgsConstructor
@Getter
public class RamenReviewBadgeEvent {

    private Long userId;
    private List<Long> menuIdList;
    private Long storeId;

}
