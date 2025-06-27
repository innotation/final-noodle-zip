package noodlezip.badge.repository;

import noodlezip.badge.entity.Badge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    @Query("""
            select b
            from Badge b
            where b.badgeCategory.id =:badgeCategoryId
            order by b.badgePolicy.badgeLevel asc
            """)
    List<Badge> findMinLevelBadgeByBadgeCategoryId(Long badgeCategoryId, Pageable pageable);

//    Optional<Badge> findTopByUserIdAndBadgeCategoryIdOrderByBadgeLevelDesc(Long badgeCategoryId);

}