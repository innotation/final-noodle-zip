package noodlezip.community.service;


import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.CategoryCountDto;
import noodlezip.community.dto.PopularTagDto;
import noodlezip.community.entity.Board;
import noodlezip.community.entity.BoardUserId;
import noodlezip.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

public interface BoardService {
    Map<String, Object> findBoardList(Pageable pageable);
    Map<String, Object> findBoardListByCategory(String category, Pageable pageable);
    Map<String, Object> searchBoardsByCommunityTypeAndKeyword(String category, String keyword, Pageable pageable);
    Map<String, Object> searchBoards(String keyword, Pageable pageable);
    Map<String, Object> findBoardLiked(Long userId, Pageable pageable);
    Map<String, Object> findBoardByUser(Long userId, Pageable pageable);
    BoardRespDto findBoardById(Long id, String userIdOrIp);
    void registBoard(BoardReqDto boardReqDto, User user);
    void deleteBoard(Long boardId, Long userId);
    boolean toggleLike(BoardUserId boardUserId);
    Integer getLikeCount(Long boardId);
    List<Board> getBoardsByIds(List<Long> recentBoardIds);
    List<Map<String, String>> uploadImages(List<MultipartFile> uploadFiles);

    // 태그 및 카테고리 조회를 위한 메소드
    List<CategoryCountDto> getCategoryCounts();
    List<PopularTagDto> getPopularTags();
    Map<String, Object> findReviewListByTag(String tag, String type, Pageable pageable);
}
