package core.application.users.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;
    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Redis 값을 등록/수정합니다.
     *
     * @param {String} key : redis key
     * @param {String} value : redis value
     * @return {void}
     */
    @Override
    public void setValue(String key, String value) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value);
    }

    /**
     * Redis 값을 등록/수정합니다.
     *
     * @param {String}   key : redis key
     * @param {String}   value: redis value
     * @param {Duration} duration: redis 값 메모리 상의 유효시간.
     * @return {void}
     */

    @Override
    public void setValue(String key, String value, Duration duration) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, duration);
    }

    /**
     * Redis 키를 기반으로 값을 조회합니다.
     *
     * @param {String} key : redis key
     * @return {String} redis value 값 반환 or 미 존재시 빈 값 반환
     */
    @Override
    public String getValue(String key) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        if (values.get(key) == null)
            return "Invalid Key";
        return String.valueOf(values.get(key));
    }

    /**
     * Redis 키값을 기반으로 row 삭제합니다.
     *
     * @param key
     */
    @Override
    public boolean deleteValue(String key) {
        logger.info("Deleting value for key: {}", key); // 삭제할 키를 로그에 기록

        Boolean result = redisTemplate.delete(key); // 삭제 결과를 Boolean으로 받음
        return result;
    }
}
