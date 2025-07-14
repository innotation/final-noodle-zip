package noodlezip.community.repository;

import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.CategoryCountDto;
import noodlezip.community.dto.PopularTagDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardRepositoryCustom {
    Optional<BoardRespDto> findBoardByBoardIdWithUser(Long boardId);
    Page<BoardRespDto> findBoardWithPagination(Pageable pageable);
    Page<BoardRespDto> findBoardByCommunityTypeWithPagination(String category, Pageable pageable);
    List<BoardRespDto> findPopularBoards(String category);
    Long increaseViewCount(Long boardId, Long viewCount);
    Page<BoardRespDto> findBoardByCommunityTypeAndKeywordWithPagination(String category, String keyword, Pageable pageable);
    Page<BoardRespDto> findBoardByKeywordWithPagination(String keyword, Pageable pageable);

    Page<BoardRespDto> findBoardByWriterAndCommunityTypeWithPagination(Long userId, String category, Pageable pageable);
    Page<BoardRespDto> findBoardsByIdsAndStatusPostedWithPaging(List<Long> boardIds, String category, Pageable pageable);
    List<CategoryCountDto> findCategoryCountsByUser(Long userId);
    List<CategoryCountDto> findCategoryCountsByBoardIds(List<Long> boardIdList);

    // 태그별 리뷰 게시글 조회
    Page<BoardRespDto> findReviewBoardsByTag(String tag, String type, Pageable pageable);
    List<CategoryCountDto> findCategoryCounts();
    List<PopularTagDto> findPopularTags();
}
