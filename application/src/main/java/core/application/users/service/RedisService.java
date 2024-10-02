package core.application.users.service;

import java.time.Duration;

public interface RedisService {

    void setValue(String key, String value);

    void setValue(String key, String value, Duration duration);

    String getValue(String key);

    boolean deleteValue(String key);
}
