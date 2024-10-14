package core.application.security.service;

public interface RedisService {
    void setValueWithTTL(String key, String value);

    String getValue(String key);

    void deleteValue(String key);
}
