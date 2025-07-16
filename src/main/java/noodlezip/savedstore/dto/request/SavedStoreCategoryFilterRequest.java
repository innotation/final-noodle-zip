package noodlezip.savedstore.dto.request;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SavedStoreCategoryFilterRequest {

    private List<Long> categoryIdList;
    private boolean isAllCategory;

}
