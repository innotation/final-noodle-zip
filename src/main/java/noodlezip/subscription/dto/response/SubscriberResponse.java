package noodlezip.subscription.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SubscriberResponse {

    private Long userSubscriptionId;
    private Long userId;
    private String loginId;
    private String name;
    private String profileImageUrl;
    private String profileBannerImageUrl;
    private Boolean isSubscribed;

}
