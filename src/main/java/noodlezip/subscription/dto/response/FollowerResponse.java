package noodlezip.subscription.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FollowerResponse {

    private Long userSubscriptionId;
    private Long followerId;
    private String name;
    private String profileImageUrl;
    private Boolean isSubscribed;

}
