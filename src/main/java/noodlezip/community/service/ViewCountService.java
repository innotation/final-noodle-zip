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
    private static final String TOTAL_VIEW_KEY_SUFFIX = ":views:";
    private static final String VIEWED_CHECK_KEY_SUFFIX = ":viewed:";

    // TTL value
    private static final Integer DUPLICATE_BOARD_VIEW_CHECK_TTL = 1;

    public void increaseViewCount(TargetType type,Long targetId, String userIdOrIp) {

        String userViewedCheckKey = type.getValue() + VIEWED_CHECK_KEY_SUFFIX + targetId + ":" + userIdOrIp;

        if (redisRepository.setIfAbsent(userViewedCheckKey, "1", DUPLICATE_BOARD_VIEW_CHECK_TTL, TimeUnit.HOURS)) {
            String totalViewCheckKey = type.getValue() + TOTAL_VIEW_KEY_SUFFIX + targetId;
            Long viewedCount = redisRepository.increase(totalViewCheckKey);
            log.debug("viewedCount: {}", viewedCount);
            log.debug("totalViewCheckKey: {}", totalViewCheckKey);
        } else {
            log.debug("User {} viewed check already exists", userIdOrIp);
        }
    }

    public Long getViewCount(TargetType type, Long targetId) {
        String totalViewCheckKey = type.getValue() + TOTAL_VIEW_KEY_SUFFIX + targetId;

        return Long.getLong(redisRepository.get(totalViewCheckKey).orElseThrow(() -> new RuntimeException("totalViewCheckKey not found")));
    }

    public void syncViewCountToDB(Long boardId) {
        // 이후 배치 등으로 구현
    }

}
