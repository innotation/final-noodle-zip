package noodlezip.community.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuReviewDto {

    private Long reviewId;
    private Long menuId;

    private int noodleThickness;
    private int noodleTexture;
    private int noodleBoiledLevel;
    private int soupThickness;
    private int soupTemperature;
    private int soupSaltiness;
    private int soupSpiciness;
    private int soupOiliness;

    private List<Long> toppingIds;

    private String content;
    private MultipartFile imageFile;
}
