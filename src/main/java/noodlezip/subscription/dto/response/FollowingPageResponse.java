package noodlezip.subscription.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class FollowingPageResponse {

    private int page;
    private int totalPage;
    private int beginPage;
    private int endPage;
    private boolean isFirst;
    private boolean isLast;

    private List<FolloweeResponse> followingList;


    public static FollowingPageResponse of(Map<String, Object> map) {
        return FollowingPageResponse.builder()
                .page((Integer) map.get("page"))
                .totalPage((Integer) map.get("totalPage"))
                .beginPage((Integer) map.get("beginPage"))
                .endPage((Integer) map.get("endPage"))
                .isFirst((Boolean) map.get("isFirst"))
                .isLast((Boolean) map.get("isLast"))
                .build();
    }

}
