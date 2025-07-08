package noodlezip.community.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.common.redis.RedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ViewCountService {

    private final RedisRepository redisRepository;

    // redis 접두사 및 만료 시간 세팅
    private static final String BOARD_TOTAL_VIEW_KEY_PREFIX = "board:views:";
    private static final String USER_BOARD_VIEWED_CHECK_KEY_PREFIX = "user:viewed:board:";

    // TTL value
    private static final Integer DUPLICATE_BOARD_VIEW_CHECK_TTL = 1;

    public void increaseViewCount(Long boardId, String userIdOrIp) {

        String userViewedCheckKey = USER_BOARD_VIEWED_CHECK_KEY_PREFIX + userIdOrIp;

        if (redisRepository.setIfAbsent(userViewedCheckKey, "1", DUPLICATE_BOARD_VIEW_CHECK_TTL, TimeUnit.HOURS)) {
            String totalViewCheckKey = BOARD_TOTAL_VIEW_KEY_PREFIX + boardId;
            Long viewedCount = redisRepository.increase(totalViewCheckKey);
            log.debug("viewedCount: {}", viewedCount);
            log.debug("totalViewCheckKey: {}", totalViewCheckKey);
        } else {
            log.debug("User {} viewed check already exists", userIdOrIp);
        }
    }

    public Long getViewCount(Long boardId) {
        String totalViewCheckKey = BOARD_TOTAL_VIEW_KEY_PREFIX + boardId;

        return Long.getLong(redisRepository.get(totalViewCheckKey).orElseThrow(() -> new RuntimeException("totalViewCheckKey not found")));
    }

    public void syncViewCountToDB(Long boardId) {
        // 이후 배치 등으로 구현
    }

}
