package core.application.security;

public interface RedisService {
    void setValueWithTTL(String key, String value);

    String getValue(String key);

    boolean deleteValue(String key);
}
