package noodlezip.common.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Repository
@Transactional
public class RedisRepository {

    private final StringRedisTemplate redisTemplate;

    public Long increase(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Set<String> getKeys(String key) {
        return redisTemplate.keys(key + "*");
    }

    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     *
     * @param key 키
     * @param value 값
     * @param expire 만료 시간(숫자)
     * @param timeUnit 시간 단위 (ex: TimeUnit.MINUTES, TimeUnit.SECONDS)
     */
    public void setWithExpire(String key, String value, long expire, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expire, timeUnit);
    }

    public Boolean setIfAbsent(String key, String value, long expire, TimeUnit timeUnit) {
        return redisTemplate.opsForValue().setIfAbsent(key, value, expire, timeUnit);
    }

    public Optional<String> get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key));
    }

    /**
     *
     * @param key 삭제할 키
     * @return 삭제 성공 여부
     */
    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    /**
     *
     * @param key 확인할 Redis 키
     * @return 존재 여부
     */
    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }
}
