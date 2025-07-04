package noodlezip.ramen.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_ramen_review")
public class RamenReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ramen_review_id", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "community_id", nullable = false)
    private Long communityId;

    @NotNull
    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Size(max = 50)
    @NotNull
    @Column(name = "noodle_thickness", nullable = false, length = 50)
    private String noodleThickness;

    @Size(max = 50)
    @NotNull
    @Column(name = "noodle_texture", nullable = false, length = 50)
    private String noodleTexture;

    @Size(max = 50)
    @NotNull
    @Column(name = "noodle_boil_level", nullable = false, length = 50)
    private String noodleBoilLevel;

    @Size(max = 50)
    @NotNull
    @Column(name = "soup_density", nullable = false, length = 50)
    private String soupDensity;

    @Size(max = 50)
    @NotNull
    @Column(name = "soup_temperature", nullable = false, length = 50)
    private String soupTemperature;

    @Size(max = 50)
    @NotNull
    @Column(name = "soup_saltiness", nullable = false, length = 50)
    private String soupSaltiness;

    @Size(max = 50)
    @NotNull
    @Column(name = "soup_spiciness_level", nullable = false, length = 50)
    private String soupSpicinessLevel;

    @Size(max = 50)
    @NotNull
    @Column(name = "soup_oiliness", nullable = false, length = 50)
    private String soupOiliness;

    @Size(max = 100)
    @Column(name = "soup_flavor_keywords", length = 100)
    private String soupFlavorKeywords;

    @Lob
    @Column(name = "content")
    private String content;

    @Size(max = 500)
    @Column(name = "review_image_url", length = 500)
    private String reviewImageUrl;

    @Column(name = "is_receipt_review")
    private Boolean isReceiptReview;

}