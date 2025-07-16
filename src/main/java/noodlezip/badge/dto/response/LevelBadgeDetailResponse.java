package noodlezip.badge.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class LevelBadgeDetailResponse {

    private Long badgeId;
    private String levelTitle;
    private String imageUrl;
    private LocalDateTime obtainedDate;

}
