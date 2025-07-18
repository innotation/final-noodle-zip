package noodlezip.store.dto;

import lombok.*;
import noodlezip.store.entity.Menu;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {

    private Long id;

    private String menuName;
    private Integer price;
    private String menuDescription;

    private String menuImageUrl;
    private String existingMenuImageUrl;

    private MultipartFile menuImageFile;

    private Integer ramenCategoryId;
    private Integer ramenSoupId;

    private List<Long> defaultToppingIds;

    private List<ExtraToppingRequestDto> extraToppings;

    public static MenuRequestDto fromEntity(Menu menu) {
        return MenuRequestDto.builder()
                .id(menu.getId())
                .menuName(menu.getMenuName())
                .price(menu.getPrice())
                .menuDescription(menu.getMenuDescription())
                .menuImageUrl(menu.getMenuImageUrl())
                .ramenCategoryId(menu.getCategory() != null ? menu.getCategory().getId().intValue() : null)
                .ramenSoupId(menu.getRamenSoup() != null ? menu.getRamenSoup().getId().intValue() : null)
                .build();
    }
}