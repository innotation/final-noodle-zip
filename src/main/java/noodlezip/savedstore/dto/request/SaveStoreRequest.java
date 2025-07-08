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
public class SaveStoreRequest {

    @NotNull(message = SavedStoreErrorStatus._DTO_FAIL_SAVED_STORE)
    private Long storeId;

    @NotNull(message = SavedStoreErrorStatus._DTO_FAIL_SAVED_STORE)
    private Long saveStoreCategoryId;

    @Size(max = 30, message = SavedStoreErrorStatus._DTO_INVALID_SAVED_STORE_CATEGORY_NAME)
    private String newSavedStoreCategoryName;

    @Size(max = 300, message = SavedStoreErrorStatus._DTO_INVALID_SAVED_STORE_CATEGORY_MEMO)
    private String memo;

}

