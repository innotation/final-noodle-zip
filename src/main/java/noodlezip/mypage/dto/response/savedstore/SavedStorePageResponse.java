package noodlezip.mypage.dto.response.savedstore;

import lombok.*;
import noodlezip.savedstore.dto.response.SavedStoreCategoryResponse;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class SavedStorePageResponse {

    private List<SavedStoreCategoryResponse> searchFilter;
    private SavedStoreListWithPageInfoResponse savedStoreList;

}
