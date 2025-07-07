package noodlezip.community.service;

import lombok.RequiredArgsConstructor;
import noodlezip.common.redis.RedisRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ViewCountService {

    private final RedisRepository redisRepository;


}
