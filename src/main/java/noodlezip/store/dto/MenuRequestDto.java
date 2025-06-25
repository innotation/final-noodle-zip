package noodlezip.store.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {

    private String menuName;
    private Integer price;
    private String menuDescription;
    private String menuImageUrl;

    private Integer ramenCategoryId; // tbl_ramen_category 참조
    private Integer ramenSoupId;     // tbl_ramen_soup 참조
}
