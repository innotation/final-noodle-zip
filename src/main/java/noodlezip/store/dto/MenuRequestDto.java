package noodlezip.store.dto;

import lombok.*;
import noodlezip.store.entity.Menu;

import java.util.List;

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

    private Integer ramenCategoryId;
    private Integer ramenSoupId;

    private List<Long> defaultToppingIds; // 기본 토핑 ID 목록 (다대다 관계)

}