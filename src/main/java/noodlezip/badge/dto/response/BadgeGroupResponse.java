package noodlezip.badge.dto.response;

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
