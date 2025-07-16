package noodlezip.savedstore.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SavedStoreCategoryResponse {

    private Long categoryId;
    private String categoryName;
    private boolean isActive;

}
