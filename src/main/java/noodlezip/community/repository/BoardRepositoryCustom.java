package noodlezip.community.repository;

import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.CategoryCountDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Optional<BoardRespDto> findBoardByBoardIdWithUser(Long boardId);
    Page<BoardRespDto> findBoardWithPagination(Pageable pageable);
    Page<BoardRespDto> findBoardByWriterWithPagination(Long userId, Pageable pageable);
    Page<BoardRespDto> findBoardsByIdsAndStatusPostedWithPaging(List<Long> boardIds, Pageable pageable);
    Page<BoardRespDto> findBoardByCommunityTypeWithPagination(String category, Pageable pageable);
    Long increaseViewCount(Long boardId, Long viewCount);
    Page<BoardRespDto> findBoardByCommunityTypeAndKeywordWithPagination(String category, String keyword, Pageable pageable);
    Page<BoardRespDto> findBoardByKeywordWithPagination(String keyword, Pageable pageable);
    List<CategoryCountDto> findCategoryCounts();
}
