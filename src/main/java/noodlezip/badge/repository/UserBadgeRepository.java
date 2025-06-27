package noodlezip.badge.repository;

import noodlezip.badge.entity.UserBadge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long> {

    @Query("""
            select ub
            from UserBadge ub
            join fetch ub.badge b
            where ub.userId = :userId
            and b.badgeCategory.id = :badgeCategoryId
            order by b.badgePolicy.badgeLevel desc
            """)
    List<UserBadge> findByUserIdAndBadgeCategoryIdWithBadge(
            long userId,
            long badgeCategoryId,
            Pageable pageable
    );

    @Query("""
        select ub
        from UserBadge ub
        join fetch ub.badge b
        where ub.userId = :userId
        and b.badgeCategory.id = :badgeCategoryId
        and b.badgeExtraOption.ramenCategory.id = :ramenCategoryId
        and b.badgePolicy.badgeLevel = (
            select max(b2.badgePolicy.badgeLevel)
            from UserBadge ub2
            join ub2.badge b2
            where ub2.userId = :userId
            and b2.badgeCategory.id = :badgeCategoryId
            and b2.badgeExtraOption.ramenCategory.id = :ramenCategoryId
        )
        order by ub.obtainedAt desc
            """)
    List<UserBadge> findByUserIdAndBadgeCategoryIdAndRamenCategoryIdWithBadge(
            long userId,
            long badgeCategoryId,
            int ramenCategoryId,
            Pageable pageable
    );


}