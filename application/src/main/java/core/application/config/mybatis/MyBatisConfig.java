package core.application.config.mybatis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("mybatis")
public class MyBatisConfig {

    @Bean
    public UUIDTypeHandler uuidTypeHandler() {
        return new UUIDTypeHandler();
    }

}
