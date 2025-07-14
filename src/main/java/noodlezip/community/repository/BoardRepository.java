package noodlezip.community.repository;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.entity.Board;
import noodlezip.community.entity.CommunityActiveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {
    List<Board> findBoardOrderByViewsCount(Integer viewsCount);

    List<Board> findBoardOrderByLikesCount(Integer likesCount);

    List<Board> findTop3ByPostStatusOrderByLikesCountDesc(CommunityActiveStatus postStatus);
}
