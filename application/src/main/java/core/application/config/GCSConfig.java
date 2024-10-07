package core.application.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.InputStream;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

/**
 * {@code GCS} 를 이용하기 위한 {@code Configuration}
 */
@Slf4j
@Configuration
public class GCSConfig {

    // GCS 서비스 계정 Key 파일 위치
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileLocation;

    /**
     * {@code GCS} 객체인 {@link Storage} 를 {@code bean} 으로 등록
     */
    @Bean
    public Storage cloudStorage() {
        try (InputStream keyFile = ResourceUtils.getURL(keyFileLocation).openStream()) {
            return StorageOptions.newBuilder()
                    .setCredentials(GoogleCredentials.fromStream(keyFile))
                    .build()
                    .getService();
        } catch (Throwable e) {
            log.error("Failed to load GCS Storage to Spring Bean", e);
            throw new RuntimeException(e);
        }
    }
}
