package noodlezip.mypage.dto.request.savedstore;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
/// 저장 가게 필터 조회 요청, 지도 좌표 찍을 카테고리
public class SavedStoreCategoryFilterRequest {

    private List<Long> categoryIdList;
    private boolean isAllCategory;

}
/**
 *
 */
