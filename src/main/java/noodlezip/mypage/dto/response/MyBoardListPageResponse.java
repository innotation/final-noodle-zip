package noodlezip.mypage.dto.response;

import lombok.*;
import noodlezip.community.dto.BoardRespDto;

import java.util.List;
import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class MyBoardListPageResponse {

    private int page;
    private int totalPage;
    private int beginPage;
    private int endPage;
    private boolean isFirst;
    private boolean isLast;

    private List<BoardRespDto> boardList;


    public static MyBoardListPageResponse of(Map<String, Object> map) {
        return MyBoardListPageResponse.builder()
                .page((Integer) map.get("page"))
                .totalPage((Integer) map.get("totalPage"))
                .beginPage((Integer) map.get("beginPage"))
                .endPage((Integer) map.get("endPage"))
                .isFirst((Boolean) map.get("isFirst"))
                .isLast((Boolean) map.get("isLast"))
                .build();
    }

}
