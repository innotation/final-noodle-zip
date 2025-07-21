package noodlezip.community.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import noodlezip.common.validation.ValidationGroups;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuReviewDto {

    @NotNull
    private Long menuId;

    private int noodleThickness;
    private int noodleTexture;
    private int noodleBoiledLevel;
    private int soupThickness;
    private int soupTemperature;
    private int soupSaltiness;
    private int soupSpiciness;
    private int soupOiliness;
    private String imageUrl;
    private String soupFlavorKeywords;

    @NotNull(message = "내용은 비어있을 수 없습니다")
    @NotBlank(message = "내용은 비어있을 수 없습니다")
    private String content;

    private List<Long> toppingIds;
}
