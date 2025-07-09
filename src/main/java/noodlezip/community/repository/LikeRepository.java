package noodlezip.community.repository;

import noodlezip.community.entity.BoardUserId;
import noodlezip.community.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, BoardUserId> {}
