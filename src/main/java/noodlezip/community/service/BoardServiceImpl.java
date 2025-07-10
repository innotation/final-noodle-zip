package noodlezip.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.exception.CustomException;
import noodlezip.common.status.ErrorStatus;
import noodlezip.community.dto.BoardReqDto;
import noodlezip.community.dto.BoardRespDto;
import noodlezip.community.entity.Board;
import noodlezip.community.entity.BoardUserId;
import noodlezip.community.entity.CommunityActiveStatus;
import noodlezip.community.entity.Like;
import noodlezip.community.repository.BoardRepository;
import noodlezip.common.repository.ImageRepository;
import noodlezip.common.util.FileUtil;
import noodlezip.common.util.PageUtil;
import noodlezip.community.repository.LikeRepository;
import noodlezip.user.entity.User;
import noodlezip.user.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final PageUtil pageUtil;
    private final ModelMapper modelMapper;
    private final FileUtil fileUtil;
    private final ViewCountService viewCountService;
    private final LikeRepository likeRepository;
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
    public BoardRespDto findBoardById(Long id, String userIdOrIp) {

        String[] infoAndIdOrIp = userIdOrIp.split(":");

        boolean isLike = false;

        if (infoAndIdOrIp[0].equals("user")) {
            isLike = likeRepository.existsById(BoardUserId.builder().userId(Long.parseLong(infoAndIdOrIp[1])).communityId(id).build());
        }

        BoardRespDto boardRespDto = boardRepository.findBoardByBoardIdWithUser(id);

        if (boardRespDto == null) {
            throw new CustomException(ErrorStatus._DATA_NOT_FOUND);
        } else {
            boardRespDto.setIsLike(isLike);
        }

        viewCountService.increaseViewCount(TargetType.BOARD, id, userIdOrIp);

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

    @Override
    public boolean toggleLike(BoardUserId boardUserId) {
        Board board = boardRepository.findById(boardUserId.getCommunityId())
                .orElseThrow(() -> new CustomException(ErrorStatus._DATA_NOT_FOUND));

        boolean isLiked;
        Optional<Like> existingLike = likeRepository.findById(boardUserId);

        if (existingLike.isPresent()) {
            likeRepository.delete(existingLike.get());
            board.setLikesCount(board.getLikesCount() - 1);
            isLiked = false;
        } else {
            Like newLike = Like.builder()
                    .id(boardUserId)
                    .createdAt(LocalDateTime.now())
                    .build();
            likeRepository.save(newLike);
            board.setLikesCount(board.getLikesCount() + 1);
            isLiked = true;
        }

        boardRepository.save(board);
        return isLiked;
    }

    @Override
    public Integer getLikeCount(Long boardId) {
        return boardRepository.findById(boardId)
                .map(Board::getLikesCount)
                .orElseThrow(() -> new CustomException(ErrorStatus._DATA_NOT_FOUND));
    }

    @Override
    public List<Board> getBoardsByIds(List<Long> recentBoardIds) {
        return boardRepository.findAllById(recentBoardIds);
    }
}
