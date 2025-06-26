package noodlezip.badge.repository;

import noodlezip.badge.entity.Badge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BadgeRepository extends JpaRepository<Badge, Long> {

    @Query("""
            select b.id
            from Badge b
            where b.badgeCategory.id =:badgeCategoryId
            order by b.badgePolicy.badgeLevel asc
            """)
    List<Long> findMinLevelBadgeByBadgeCategoryId(Long badgeCategoryId, Pageable pageable);

}