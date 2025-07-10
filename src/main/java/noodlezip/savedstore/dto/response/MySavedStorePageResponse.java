package noodlezip.savedstore.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MySavedStorePageResponse {

    private List<SavedStoreCategoryResponse> searchFilter;
    private List<SavedStoreCategoryResponse> updateCategoryList;
    private SavedStoreListWithPageInfoResponse savedStoreList;

}
