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
    private String noodleThickness;
    private String noodleTexture;
    private String noodleBoilLevel;
    private String soupTemperature;
    private String soupSaltiness;
    private String soupSpicinessLevel;
    private String soupOiliness;
    private String soupFlavorKeywords;
    private String content;
    private String reviewImageUrl;
    private Boolean isReceiptReview;
    private List<String> toppingNames;

    public StoreReviewDto(Long id, Long communityId, Long menuId, String menuName, String noodleThickness, String noodleTexture, String noodleBoilLevel, String soupTemperature, String soupSaltiness, String soupSpiciness, String soupOiliness, String soupFlavorKeywords, String content, String reviewImageUrl, Boolean isReceiptReview) {
        this.id = id;
        this.communityId = communityId;
        this.menuId = menuId;
        this.menuName = menuName;
        this.noodleThickness = noodleThickness;
        this.noodleTexture = noodleTexture;
        this.noodleBoilLevel = noodleBoilLevel;
        this.soupTemperature = soupTemperature;
        this.soupSaltiness = soupSaltiness;
        this.soupSpicinessLevel = soupSpiciness;
        this.soupOiliness = soupOiliness;
        this.soupFlavorKeywords = soupFlavorKeywords;
        this.content = content;
        this.reviewImageUrl = reviewImageUrl;
        this.isReceiptReview = isReceiptReview;
    }

}
