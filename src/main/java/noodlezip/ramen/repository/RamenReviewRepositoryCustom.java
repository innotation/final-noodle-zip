package noodlezip.ramen.repository;

import noodlezip.store.dto.ReviewSummaryDto;
import noodlezip.store.dto.StoreReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface RamenReviewRepositoryCustom {

    Page<StoreReviewDto> findReviewsByStoreId(Long storeId, Pageable pageable);

    boolean existsByOcrKeyHash(String ocrKeyHash);

    ReviewSummaryDto getSummaryByStoreId(Long storeId);

    ReviewSummaryDto getSummaryByStoreIdAndMenuName(Long storeId, String menuName);

    // 카테고리별 리뷰 개수 조회
    Map<String, Long> getReviewCountByCategory();

    // 육수별 리뷰 개수 조회
    Map<String, Long> getReviewCountBySoup();

    // 태그로 필터링된 리뷰 목록 조회
    Page<StoreReviewDto> findReviewsByTag(String tag, String type, Pageable pageable);

    Page<StoreReviewDto> findReviewsByUserId(Long userId, Pageable pageable);
}
