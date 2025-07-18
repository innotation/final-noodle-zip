package noodlezip.badge.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class RamenReviewBadgeEvent {

    private Long userId;
    private Long storeId;
    private List<Long> menuIdList;

}
