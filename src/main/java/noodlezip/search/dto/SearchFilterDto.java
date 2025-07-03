package noodlezip.search.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class SearchFilterDto {

    private Double lat;
    private Double lng;
    private List<String> region;
    private List<String> ramenCategory;
    private List<String> ramenSoup;
    private List<String> topping;

    // 검색어 필터
    private String searchType; // "STORE_NAME", "MENU_NAME", "ALL"
    private String keyword;    // 검색어

}
