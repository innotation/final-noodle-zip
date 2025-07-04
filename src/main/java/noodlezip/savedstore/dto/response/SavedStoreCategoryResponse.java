package noodlezip.savedstore.dto.response;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class SavedStoreCategoryResponse {
    /// 사용자가 가게 저장하기 Whishlist를 클릭했을 때 띄울 카테고리 리스트

    private Long categoryId;
    private String categoryName;
    private boolean isDefault;    /// isDefault가 true인 값은 [라디오 버튼]이 선택된 상태로 띄워집니다.

}


/**
 * 1. 가게 상세페이지 접근
 * <p>
 * 2. Wishlist 버튼 클릭
 * <p>
 * 3. 모달로 저장 from 작성
 * - 기존 카테고리1(deafult)
 * - 기존 카테고리2
 * - [textbox]  새로운 카테고리 텍스트 입력
 * <p>
 * 4. 한 줄 메모 텍스트 입력
 * <p>
 * 5. 저장 버튼 클릭
 */