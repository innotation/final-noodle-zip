package noodlezip.store.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

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

    private MultipartFile menuImageFile;

    private Integer ramenCategoryId;
    private Integer ramenSoupId;

    private List<Long> defaultToppingIds;

    private List<ExtraToppingRequestDto> extraToppings;
}