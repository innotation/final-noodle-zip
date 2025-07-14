package noodlezip.store.dto;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@Builder
public class StoreReviewDto {

    private Long id;
    private Long communityId;
    private Long menuId;
    private String menuName;
    private Integer noodleThickness;
    private Integer noodleTexture;
    private Integer noodleBoilLevel;
    private Integer soupDensity;
    private Integer soupTemperature;
    private Integer soupSaltiness;
    private Integer soupSpicinessLevel;
    private Integer soupOiliness;
    private String soupFlavorKeywords;
    private String content;
    private String reviewImageUrl;
    private Boolean isReceiptReview;
    private List<String> toppingNames;
    private String userName;
    private Long userId;
    private java.time.LocalDateTime createdAt;
    private java.time.LocalDateTime updatedAt;

}
