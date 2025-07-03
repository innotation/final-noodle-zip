package noodlezip.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.entity.Board;
import noodlezip.community.repository.BoardRepository;
import noodlezip.common.entity.Image;
import noodlezip.common.repository.ImageRepository;
import noodlezip.common.util.FileUtil;
import noodlezip.common.util.PageUtil;
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

    @Override
    @Transactional(readOnly = true)
    public Map<String, Object> findBoardList(Pageable pageable) {
        Page<Board> boardPage = boardRepository.findAll(pageable);

        Map<String, Object> map = pageUtil.getPageInfo(boardPage, 5);

        map.put("list", boardPage.getContent().stream().map(data -> {
            return modelMapper.map(data, Board.class);
        }).toList());

        return map;
    }

    @Override
    @Transactional(readOnly = true)
    public Board findBoardById(long id) {
        return boardRepository.findBoardById(id);
    }

    @Override
    public void registBoard(BoardReqDto boardReqDto, long userId,MultipartFile boardImage) {
        Board board = modelMapper.map(boardReqDto, Board.class);
        board.setCommunityType("community");
        board.setUserId(userId);
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
}
