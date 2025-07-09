package noodlezip.savedstore.dto.response;

import lombok.*;

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

