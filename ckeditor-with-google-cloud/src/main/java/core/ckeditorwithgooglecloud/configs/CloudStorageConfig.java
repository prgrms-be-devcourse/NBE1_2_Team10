package core.ckeditorwithgooglecloud.configs;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.IOException;
import java.io.InputStream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;

// GCS 객체 생성용 configuration
// GCSService 에서 Storage 주입 필요함.
@Configuration
public class CloudStorageConfig {

    // 발급받은 서비스 계정 key 파일 위치
    @Value("${spring.cloud.gcp.storage.credentials.location}")
    private String keyFileLocation;

    // GCS 에서 발급받은 key 파일 읽어서
    // GCS 객체 (com.google.cloud.storage.Storage) 생성
    @Bean
    public Storage cloudStorage() throws IOException {

        // key 파일 위치에서 IO stream 생성
        try (InputStream keyFile = ResourceUtils.getURL(keyFileLocation).openStream()) {

            // IO stream 에서 필요한 것들 잘 읽음
            return StorageOptions.newBuilder()
                    .setCredentials(    // Credential 도 뭐 어케 잘 읽나봄..?
                            GoogleCredentials.fromStream(keyFile))
                    .build()
                    .getService();
        }
    }
}
