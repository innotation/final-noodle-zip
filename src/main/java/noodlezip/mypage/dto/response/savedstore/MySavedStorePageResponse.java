package noodlezip.mypage.dto.response.savedstore;

import lombok.*;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class MySavedStorePageResponse {

    /// my, {userId}
    private List<SavedStoreCategoryResponse> searchFilter;
    private List<SavedStoreCategoryResponse> updateCategoryList;
    private SavedStoreListWithPageInfoResponse savedStoreList;

}
