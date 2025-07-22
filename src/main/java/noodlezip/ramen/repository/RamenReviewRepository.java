package noodlezip.ramen.repository;

import noodlezip.ramen.entity.RamenReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import noodlezip.store.dto.StoreReviewDto;

public interface RamenReviewRepository extends JpaRepository<RamenReview, Long>, RamenReviewRepositoryCustom {
    List<StoreReviewDto> findAllReviewsByUserId(Long userId);
}