package noodlezip.store.dto;

import lombok.*;

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

    private Integer ramenCategoryId; // ramenCategory 엔티티의 ID
    private Integer ramenSoupId;     // ramenSoup 엔티티의 ID

    private List<Long> defaultToppingIds; // 기본 토핑 ID 목록 (다대다 관계)
}
