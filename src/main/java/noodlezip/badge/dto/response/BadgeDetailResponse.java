package noodlezip.badge.dto.response;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class BadgeDetailResponse {

    private String mainTitle;
    private String description;
    private String mainImageUrl;
    private LocalDateTime obtainedDate;
    private List<LevelBadgeDetailResponse> levelBadgeDetailList;

}
