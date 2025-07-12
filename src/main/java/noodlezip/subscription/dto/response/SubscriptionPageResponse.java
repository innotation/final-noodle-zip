package noodlezip.subscription.dto.response;

import lombok.*;
import noodlezip.subscription.constants.SubscriptionType;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SubscriptionPageResponse {

    private int page;
    private int totalPage;
    private int beginPage;
    private int endPage;
    private boolean isFirst;
    private boolean isLast;

    private Long requestUserId;
    private Long targetUserId;
    private SubscriptionType subscriptionType;
    private List<SubscriberResponse> subscriptionList;


    public static SubscriptionPageResponse pageOf(Map<String, Object> map) {
        return SubscriptionPageResponse.builder()
                .page((Integer) map.get("page"))
                .totalPage((Integer) map.get("totalPage"))
                .beginPage((Integer) map.get("beginPage"))
                .endPage((Integer) map.get("endPage"))
                .isFirst((Boolean) map.get("isFirst"))
                .isLast((Boolean) map.get("isLast"))
                .build();
    }

}
