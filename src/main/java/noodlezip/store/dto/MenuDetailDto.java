package noodlezip.store.dto;

import lombok.*;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MenuDetailDto {
    private Long menuId;
    private String menuName;
    private Integer price;
    private String menuDescription;
    private String menuImageUrl;
    private String categoryName;
    private String soupName;
    private List<String> toppingNames;
}