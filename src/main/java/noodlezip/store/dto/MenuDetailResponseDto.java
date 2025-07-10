package noodlezip.store.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MenuDetailResponseDto {

    private List<MenuDetailDto> menus;
    private List<String> categoryNames;
    private List<String> soupNames;
    private List<String> toppingNames;

}
