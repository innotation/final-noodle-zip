package noodlezip.ramen.repository;

import noodlezip.store.dto.ReviewSummaryDto;
import noodlezip.store.dto.StoreReviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RamenReviewRepositoryCustom {

    Page<StoreReviewDto> findReviewsByStoreId(Long storeId, Pageable pageable);

    ReviewSummaryDto getSummaryByStoreId(Long storeId);

    ReviewSummaryDto getSummaryByStoreIdAndMenuName(Long storeId, String menuName);

}
