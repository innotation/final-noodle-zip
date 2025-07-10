package noodlezip.savedstore.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.savedstore.status.SavedStoreErrorStatus;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class AddCategoryRequest {

    @Size(max = 30, message = SavedStoreErrorStatus._DTO_INVALID_SAVED_STORE_CATEGORY_NAME)
    private String categoryName;

    @NotNull(message = SavedStoreErrorStatus._DTO_FAIL_SAVED_STORE)
    private boolean isPublic;

}
