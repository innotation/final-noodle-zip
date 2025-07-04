package noodlezip.store.dto;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuRequestDto {

    private String menuName;
    private Integer price;
    private String menuDescription;

    // DB에 저장할 URL
    private String menuImageUrl;

    // 클라이언트에서 전달할 MultipartFile 이미지
    private MultipartFile menuImageFile;

    private Integer ramenCategoryId;
    private Integer ramenSoupId;

    private List<Long> defaultToppingIds; // 기본 토핑 ID 목록 (다대다 관계)

    private List<String> extraToppings;   // 추가 토핑
}