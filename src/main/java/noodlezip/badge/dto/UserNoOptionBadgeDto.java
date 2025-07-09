package noodlezip.badge.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserNoOptionBadgeDto {

    private Long userBadgeId;
    private Long badgeId;
    private Long badgeCategoryId;
    private Long badgeGroupId;
    private String badgeCategoryName;
    private String badgeName;
    private Integer accumulativeValue;
    private String badgeImageUrl;

}
