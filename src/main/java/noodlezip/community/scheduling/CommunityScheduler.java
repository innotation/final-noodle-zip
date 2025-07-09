package noodlezip.community.scheduling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import noodlezip.community.repository.BoardRepository;
import noodlezip.community.service.TargetType;
import noodlezip.community.service.ViewCountService;
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

    private final ViewCountService viewCountService;

    @Scheduled(cron = "0 */1 * * * *")
    public void updateBoardViewCountsFromRedis() {
        log.info("--- 게시글 조회수 DB 반영 스케줄러 시작 ---");
        long startTime = System.currentTimeMillis();
        int results = viewCountService.syncBoardViewCountsToDB();
        log.info("--- 게시글 조회수 DB 반영 스케줄러 종료. 총 {}개 게시글 조회수 업데이트 (소요 시간: {}ms) ---", results, (System.currentTimeMillis() - startTime));
    }
}
