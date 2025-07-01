package noodlezip.mypage.dto;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

public class BadgeGroupResponse {

    private Long id;
    private String badgeGroupName;

}
