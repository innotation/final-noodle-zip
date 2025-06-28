package noodlezip.badge.repository;

import lombok.RequiredArgsConstructor;
import noodlezip.badge.entity.Badge;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BadgeRepository extends JpaRepository<Badge, Long>, BadgeQueryRepository {
}
