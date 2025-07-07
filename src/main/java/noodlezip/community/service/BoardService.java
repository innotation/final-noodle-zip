package noodlezip.community.service;


import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BoardService {
    Map<String, Object> findBoardList(Pageable pageable);
    Map<String, Object> findBoardListByCategory(String category, Pageable pageable);
    BoardRespDto findBoardById(Long id, Long userId);
    void registBoard(BoardReqDto boardReqDto, User user, MultipartFile boardImage);
    void deleteBoard(Long boardId);
}
