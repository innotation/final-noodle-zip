package noodlezip.community.service;


import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.dto.ReviewReqDto;
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
    List<Board> findMostLikedBoardList();
    List<Board> findMostViewedBoardList();
    Map<String, Object> findBoardListByCategory(String category, Pageable pageable);
    Map<String, Object> searchBoardsByCommunityTypeAndKeyword(String category, String keyword, Pageable pageable);
    Map<String, Object> searchBoards(String keyword, Pageable pageable);

    Map<String, Object> findBoardByUser(Long userId,  Pageable pageable);
    Map<String, Object> findBoardLikedByCategory(Long userId, List<Long> boardIdList, String category, Pageable pageable);
    Map<String, Object> findBoardByUserByCategory(Long userId, String category, Pageable pageable);
    List<CategoryCountDto> getCategoryCountsByUser(Long userId);
    List<CategoryCountDto> getCategoryCountsByBoardIds(List<Long> boardIdList);

    BoardRespDto findBoardById(Long id, String userIdOrIp);
    BoardRespDto findReviewBoardById(Long id, String userIdOrIp);
    void registBoard(BoardReqDto boardReqDto, User user, String category);
    void deleteBoard(Long boardId, Long userId);
    boolean toggleLike(BoardUserId boardUserId);
    Integer getLikeCount(Long boardId);
    List<Board> getBoardsByIds(List<Long> recentBoardIds);
    List<Map<String, String>> uploadImages(List<MultipartFile> uploadFiles);
    List<BoardRespDto> getPopularBoards(String category);
    List<CategoryCountDto> getCategoryCounts();
    List<PopularTagDto> getPopularTags();
    Map<String, Object> findReviewListByTag(String tag, String type, Pageable pageable);
    List<Long> getBoardIdByUserLiked(Long userId);

    void saveReviewJson(ReviewReqDto dto, User user);
}
