package noodlezip.savedstore.dto.response;

import lombok.*;

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
