package noodlezip.store.dto;

import lombok.*;
import noodlezip.ramen.dto.ToppingResponseDto;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrToReviewDto {
    private Long storeId;
    private String storeName;
    private List<MenuDetailDto> menuList;
    private List<ToppingResponseDto> toppingList;
}
