package noodlezip.community.repository;

import noodlezip.community.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Board findBoardById(Long id);
}
