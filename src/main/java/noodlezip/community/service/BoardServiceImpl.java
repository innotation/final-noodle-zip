package noodlezip.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.entity.Board;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.repository.BoardRepository;
import noodlezip.common.repository.ImageRepository;
import noodlezip.common.util.FileUtil;
import noodlezip.common.util.PageUtil;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final PageUtil pageUtil;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ImageRepository imageRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findBoardList(Pageable pageable) {
        Page<BoardRespDto> boardPage = boardRepository.findBoardWithPagination(pageable);

        Map<String, Object> map = pageUtil.getPageInfo(boardPage, 5);

        map.put("list", boardPage.getContent());

        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findBoardListByCategory(String category, Pageable pageable) {
        Page<BoardRespDto> boardPage = boardRepository.findBoardWithPaginationAndCommunityType(category, pageable);

        Map<String, Object> map = pageUtil.getPageInfo(boardPage, 5);

        map.put("list", boardPage.getContent());

        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public BoardRespDto findBoardById(Long id, Long userId) {
        BoardRespDto boardRespDto = boardRepository.findBoardByBoardIdWithUser(id, userId);
        if (boardRespDto == null) {
            throw new CustomException(ErrorStatus._DATA_NOT_FOUND);
        }
        return boardRespDto;
    }

    @Override
    public void registBoard(BoardReqDto boardReqDto, User user, MultipartFile boardImage) {
        Board board = modelMapper.map(boardReqDto, Board.class);
        board.setCommunityType("community");
        board.setPostStatus(CommunityActiveStatus.POSTED);
        board.setUser(user);
        if (!boardImage.isEmpty() && boardImage.getOriginalFilename() != null) {
            Map<String, String> map = fileUtil.fileupload("board", boardImage);
//            Image image = Image.builder()
//                                .imageOrder(1)
//                                .imageUrl(map.get("fileUrl"))
//                                .imageType("community")
//                                .targetId(board.getId())
//                                .build();
//            imageRepository.save(image);
            board.setImageUrl(map.get("fileUrl"));
//            log.info("image save : {}", image);
        }
        boardRepository.save(board);
        log.info("board save : {}", board);
    }

    @Override
    public void deleteBoard(Long boardId, Long userId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new CustomException(ErrorStatus._DATA_NOT_FOUND));

        if(!board.getUser().getId().equals(userId)) {
            throw new CustomException(ErrorStatus._FORBIDDEN);
        }

        boardRepository.delete(board);
    }
}
