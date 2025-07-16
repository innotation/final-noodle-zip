package noodlezip.community.repository;

import noodlezip.community.entity.BoardUserId;
import noodlezip.community.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, BoardUserId> {
    @Query("SELECT l.id.communityId FROM Like l WHERE l.id.userId = :userId")
    List<Long> findCommunityIdsByUserId(@Param("userId") Long userId);
}
