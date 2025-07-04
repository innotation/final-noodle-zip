package noodlezip.savedstore.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SavedStoreApiResponse {

    private boolean isSuccess;
    private String message;

}
