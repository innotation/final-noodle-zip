package noodlezip.community.repository;

import noodlezip.community.dto.BoardRespDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface BoardRepositoryCustom {
    Optional<BoardRespDto> findBoardByBoardIdWithUser(Long boardId);
    Page<BoardRespDto> findBoardWithPagination(Pageable pageable);
    Page<BoardRespDto> findBoardWithPaginationAndCommunityType(String category, Pageable pageable);
    Long increaseViewCount(Long boardId, Long viewCount);
    Page<BoardRespDto> findBoardWithPaginationAndCommunityTypeAndKeyword(String category, String keyword, Pageable pageable);
    Page<BoardRespDto> findBoardWithPaginationAndKeyword(String keyword, Pageable pageable);
}
