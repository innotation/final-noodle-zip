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

}
