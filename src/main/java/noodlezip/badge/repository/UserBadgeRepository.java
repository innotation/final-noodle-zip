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
            and b.badgeExtraOption.storeSidoLegalCode = :legalCode
            order by ub.id desc
            """)
    List<UserBadge> findByUserIdAndBadgeCategoryIdAndLegalCode(
            long userId,
            long badgeCategoryId,
            int legalCode
    );

}