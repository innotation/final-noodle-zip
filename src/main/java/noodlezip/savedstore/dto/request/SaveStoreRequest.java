package noodlezip.savedstore.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import noodlezip.savedstore.status.SavedStoreErrorStatus;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SaveStoreRequest {

    @NotNull(message = SavedStoreErrorStatus._DTO_FAIL_SAVED_STORE)
    private Long storeId;

    private List<Long> saveStoreCategoryIds;

    @Size(max = 300, message = SavedStoreErrorStatus._DTO_INVALID_SAVED_STORE_CATEGORY_MEMO)
    private String memo;

}

