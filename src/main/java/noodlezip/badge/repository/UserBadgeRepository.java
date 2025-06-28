package noodlezip.badge.repository;

import noodlezip.badge.entity.UserBadge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserBadgeRepository extends JpaRepository<UserBadge, Long>, UserBadgeQueryRepository {
}