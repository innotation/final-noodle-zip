package noodlezip.store.dto;

import jakarta.validation.constraints.Size;
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

    @Size(max = 100, message = "메뉴 설명은 100자 이하로 작성해주세요.")
    private String menuDescription;

    private String menuImageUrl;

    private MultipartFile menuImageFile;

    private Integer ramenCategoryId;
    private Integer ramenSoupId;

    private List<Long> defaultToppingIds;

    private List<ExtraToppingRequestDto> extraToppings;
}