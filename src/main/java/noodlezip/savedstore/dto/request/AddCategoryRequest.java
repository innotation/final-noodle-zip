package noodlezip.savedstore.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AddCategoryRequest {
    private String categoryName;
    private boolean isPublic;
}
