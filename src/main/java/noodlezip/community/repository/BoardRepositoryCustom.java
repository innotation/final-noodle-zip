package noodlezip.community.repository;

import noodlezip.community.dto.BoardRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardRepositoryCustom {
    BoardRespDto findBoardByBoardIdWithUser(Long boardId);
    Page<BoardRespDto> findBoardWithPagination(Pageable pageable);
    Page<BoardRespDto> findBoardWithPaginationAndCommunityType(String category, Pageable pageable);
    Long increaseViewCount(Long boardId, Long viewCount);
}
