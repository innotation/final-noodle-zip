package noodlezip.community.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.community.repository.BoardRepository;
import noodlezip.community.service.TargetType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommunityScheduler {

    // RedisTemplate을 직접 사용하거나, RedisRepository에 keys/get/delete 메소드를 추가하여 사용
    private final RedisTemplate<String, String> redisTemplate;
    private final BoardRepository boardRepository; // 게시글 DB 업데이트를 위한 Repository

    // ViewCountService의 접두사 사용 (상수화하여 중복 정의 방지 권장)
    private static final String BOARD_TOTAL_VIEW_KEY_PREFIX = TargetType.BOARD.getValue() + ":views:";
    private static final Pattern BOARD_ID_REGEX = Pattern.compile(BOARD_TOTAL_VIEW_KEY_PREFIX + "(\\d+)");

    @Scheduled(cron = "0 */5 * * * *")
    @Transactional
    public void updateBoardViewCountsFromRedis() {
        log.info("--- 게시글 조회수 DB 반영 스케줄러 시작 ---");
        long startTime = System.currentTimeMillis();

        Set<String> viewCountKeys = redisTemplate.keys(BOARD_TOTAL_VIEW_KEY_PREFIX + "*");

        if (viewCountKeys == null || viewCountKeys.isEmpty()) {
            log.info("Redis에 반영할 게시글 조회수 데이터가 없습니다.");
            log.info("--- 게시글 조회수 DB 반영 스케줄러 종료 (소요 시간: {}ms) ---", (System.currentTimeMillis() - startTime));
            return;
        }

        int updatedCount = 0;
        for (String key : viewCountKeys) {
            try {
                Matcher matcher = BOARD_ID_REGEX.matcher(key);
                if (!matcher.matches()) {
                    log.warn("유효하지 않은 Redis 조회수 키 패턴 발견: {}. 삭제", key);
                    redisTemplate.delete(key);
                    continue;
                }
                Long boardId = Long.parseLong(matcher.group(1));

                String viewCountDeltaStr = redisTemplate.opsForValue().get(key);

                if (viewCountDeltaStr == null) {
                    redisTemplate.delete(key);
                    continue;
                }

                Long viewCountDelta = Long.parseLong(viewCountDeltaStr);

                // 게시글 기존 조회수 + Redis의 증가분

                boardRepository.increaseViewCount(boardId, viewCountDelta);
                updatedCount++;

                // Redis 데이터 삭제

                redisTemplate.delete(key);
                log.debug("Board ID: {}에 조회수 {}를 DB에 반영 및 Redis 키를 삭제.", boardId, viewCountDelta);

            } catch (NumberFormatException e) {
                log.error("Redis 키 '{}'의 값이 유효한 숫자가 아닙니다. (값: {}) 키를 삭제합니다. 오류: {}", key, redisTemplate.opsForValue().get(key), e.getMessage());
                redisTemplate.delete(key);
            } catch (Exception e) {
                log.error("Board ID: {} 조회수 DB 반영 중 오류 발생: {}", key, e.getMessage(), e);
            }
        }

        log.info("--- 게시글 조회수 DB 반영 스케줄러 종료. 총 {}개 게시글 조회수 업데이트 (소요 시간: {}ms) ---", updatedCount, (System.currentTimeMillis() - startTime));
    }
}
