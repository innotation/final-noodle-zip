package noodlezip.mypage.dto.request.badge;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class BadgeDetailRequest {

    private Long userId;
    private Long badgeId;
    private Long badgeCategoryId;

}
