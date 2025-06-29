package noodlezip.badge.repository;

import noodlezip.badge.entity.UserBadge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long>, UserBadgeQueryRepository {
}
