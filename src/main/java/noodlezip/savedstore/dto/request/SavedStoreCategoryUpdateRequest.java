package noodlezip.savedstore.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.savedstore.status.SavedStoreErrorStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SavedStoreCategoryUpdateRequest {

    @NotNull(message = SavedStoreErrorStatus._DTO_FAIL_UPDATE_SAVED_STORE_CATEGORY)
    private Long saveStoreCategoryId;

    @Size(max = 300, message = SavedStoreErrorStatus._DTO_INVALID_SAVED_STORE_CATEGORY_MEMO)
    private String savedStoreCategoryName;

    @NotNull(message = SavedStoreErrorStatus._DTO_FAIL_UPDATE_SAVED_STORE_CATEGORY)
    private boolean isPublic;

}
