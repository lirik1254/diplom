package backend.academy.diplom;

import jakarta.annotation.PostConstruct;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

import java.net.URI;

@Configuration
public class S3Config {

    @Value("${yandex-cloud-key-id}")
    private String yandexCloudKeyId;

    @Value("${yandex-cloud-secret-key}")
    private String yandexCloudSecretKey;

    @Value("${yandex-cloud-region}")
    private String yandexCloudRegion;

    @Value("${yandex-cloud-s3-endpoint}")
    private String yandexCloudS3Endpoint;

    @Getter
    @Value("${yandex-cloud-bucket}")
    private String yandexCloudBucket;

    private StaticCredentialsProvider staticCredentialsProvider;

    @PostConstruct
    public void init() {
        this.staticCredentialsProvider = StaticCredentialsProvider.create(
                AwsBasicCredentials.create(yandexCloudKeyId, yandexCloudSecretKey)
        );
    }

    @Bean
    public S3Client getS3Client() {
        return S3Client.builder()
                .httpClient(ApacheHttpClient.create())
                .region(Region.of(yandexCloudRegion))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }

    @Bean
    public S3Presigner getS3Presigner() {
        return S3Presigner.builder()
                .region(Region.of(yandexCloudRegion))
                .endpointOverride(URI.create(yandexCloudS3Endpoint))
                .credentialsProvider(staticCredentialsProvider)
                .build();
    }
}
