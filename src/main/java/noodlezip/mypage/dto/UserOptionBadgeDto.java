package noodlezip.mypage.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class UserOptionBadgeDto {

    private Long userBadgeId;
    private Long badgeId;
    private Long badgeCategoryId;
    private Long badgeGroupId;
    private Integer storeSidoLegalCode;
    private String ramenCategoryName;
    private String badgeName;
    private Integer accumulativeValue;
    private String badgeImageUrl;

}
