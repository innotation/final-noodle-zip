package noodlezip.mypage.dto.response.badge;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BadgeGroupResponse {

    private Long id;
    private String badgeGroupName;

}
