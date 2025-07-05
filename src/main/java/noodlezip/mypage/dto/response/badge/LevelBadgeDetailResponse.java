package noodlezip.mypage.dto.response.badge;

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
