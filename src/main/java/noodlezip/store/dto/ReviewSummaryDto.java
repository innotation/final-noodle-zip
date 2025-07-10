package noodlezip.store.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ReviewSummaryDto {

    private Double noodleThickness;
    private Double noodleTexture;
    private Double noodleBoilLevel;
    private Double soupTemperature;
    private Double soupSaltiness;
    private Double soupSpicinessLevel;
    private Double soupOiliness;
    private Integer totalCount;
    private Double overall;


}
