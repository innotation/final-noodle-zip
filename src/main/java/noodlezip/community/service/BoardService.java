package noodlezip.community.service;


import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.entity.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface BoardService {
    Map<String, Object> findBoardList(Pageable pageable);
    Board findBoardById(long id);
    void registBoard(BoardReqDto boardReqDto, Long userId, MultipartFile boardImage);
}
