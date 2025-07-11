package noodlezip.store.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OcrToReviewDto {
    private String storeName;
    private List<MenuDetailDto> menuList;
    private List<String> toppings;
}
