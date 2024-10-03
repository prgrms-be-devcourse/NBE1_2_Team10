package core.application.users.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public interface RedisService {
    void setValueWithTTL(String key, String value);

    String getValue(String key);

    boolean deleteValue(String key);
}
