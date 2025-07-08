package noodlezip.mypage.dto.response.savedstore;

import lombok.*;
import noodlezip.search.dto.SearchStoreDto;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SavedStoreMapResponse {

    private Map<Integer, List<SavedStoreResponse>> locationListByCategoryId;

}

