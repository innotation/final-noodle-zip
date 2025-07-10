package noodlezip.subscription.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class FolloweeResponse {

    private Long userSubscriptionId;
    private Long followeeId;
    private String name;
    private String profileImageUrl;
    private Boolean isSubscribed;

}
