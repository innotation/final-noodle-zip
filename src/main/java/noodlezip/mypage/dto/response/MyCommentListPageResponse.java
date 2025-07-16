package noodlezip.mypage.dto.response;

import lombok.*;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MyCommentListPageResponse {

    private int page;
    private int totalPage;
    private int beginPage;
    private int endPage;
    private boolean isFirst;
    private boolean isLast;

    private Long totalCommentCount;
    private List<MyCommentResponse> commentList;


    public static MyCommentListPageResponse of(Map<String, Object> map) {
        return MyCommentListPageResponse.builder()
                .page((Integer) map.get("page"))
                .totalPage((Integer) map.get("totalPage"))
                .beginPage((Integer) map.get("beginPage"))
                .endPage((Integer) map.get("endPage"))
                .isFirst((Boolean) map.get("isFirst"))
                .isLast((Boolean) map.get("isLast"))
                .build();
    }

}
