package programmers.kmdbreal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KmdbConfig {
	@Value("${kmdb.api.url}")
	private String apiUrl;

	@Bean
	public WebClient webClient() {
		return WebClient.builder()
			.baseUrl(apiUrl)
			.build();
	}
}
