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

    // 기존 카테고리 ID들 (체크된 기존 카테고리들)
    private List<Long> saveStoreCategoryIds;

    // 새로 추가할 카테고리명들
    @Size(max = 30, message = SavedStoreErrorStatus._DTO_INVALID_SAVED_STORE_CATEGORY_NAME)
    private List<String> newSavedStoreCategoryNames;

    @Size(max = 300, message = SavedStoreErrorStatus._DTO_INVALID_SAVED_STORE_CATEGORY_MEMO)
    private String memo;

}

