package core.application.users.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${token.refresh.timeout}")
    private Long refreshTimeout;

    @Value("${token.refresh.time-unit}")
    private TimeUnit timeUnit;

    public RedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Redis 값을 등록/수정합니다.
     *
     * @param {String}   key : redis key
     * @param {String}   value : redis value
     * @param {Long} timeout : redis 값 메모리 상의 유효시간
     * @param {TimeUnit} unit : 유효 시간의 단위
     * @return {void}
     */

    @Override
    public void setValueWithTTL(String key, String value) {
        ValueOperations<String, Object> values = redisTemplate.opsForValue();
        values.set(key, value, refreshTimeout, timeUnit);
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
        Boolean result = redisTemplate.delete(key); // 삭제 결과를 Boolean으로 받음
        return result;
    }
}
