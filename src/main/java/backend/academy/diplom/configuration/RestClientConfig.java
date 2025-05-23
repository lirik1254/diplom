package backend.academy.diplom.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Value("${recommendation-url}")
    private String recommendationUrl;

    @Bean(name = "baseUrl")
    public RestClient restClientBaseUrl() {
        return RestClient
                .builder()
                .baseUrl(recommendationUrl)
                .build();
    }
}
