package backend.academy.diplom.services;

import backend.academy.diplom.S3Config;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.utils.CreateAccessToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class FileService {

    private final S3Config s3Config;
    private final S3Presigner presigner;
    private final CreateAccessToken createAccessToken;
    private final UserRepository userRepository;

    public String putLink(String filePath) {
        PutObjectRequest putObjectRequest = PutObjectRequest
                .builder()
                .bucket(s3Config.getYandexCloudBucket())
                .key(filePath)
                .build();

        PutObjectPresignRequest putObjectPresignRequest = PutObjectPresignRequest
                .builder()
                .signatureDuration(Duration.ofMinutes(10))
                .putObjectRequest(putObjectRequest)
                .build();

        PresignedPutObjectRequest presignedPutObjectRequest =
                presigner.presignPutObject(putObjectPresignRequest);

        return presignedPutObjectRequest.url().toString();
    }

    public String getLink(String authHeader, String type) {
        String token = authHeader.substring(7);

        String email = createAccessToken.getEmailFromJwtToken(token);

        User user = userRepository.findByEmail(email).getFirst();

        String s3Path = "";
        switch (type) {
            case "diploma" -> s3Path = user.getDiplomaPath();
            case "resume" -> s3Path = user.getResumePath();
            case "photo" -> s3Path = user.getPhotoPath();
        }

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Config.getYandexCloudBucket())
                .key(s3Path)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedRequest = presigner.presignGetObject(presignRequest);

        return presignedRequest.url().toString();
    }

    public String getPresignedLink(String key) {
        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(s3Config.getYandexCloudBucket())
                .key(key)
                .build();

        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(10))
                .getObjectRequest(getObjectRequest)
                .build();

        PresignedGetObjectRequest presignedGetObjectRequest = presigner.presignGetObject(presignRequest);

        return presignedGetObjectRequest.url().toString();
    }

    @Transactional
    public void saveLink(String fileType, String path, String authHeader) {
        String token = authHeader.substring(7);
        String email = createAccessToken.getEmailFromJwtToken(token);
        Long userId = userRepository.findByEmail(email).getFirst().getId();

        switch (fileType) {
            case "diploma" -> userRepository.updateFileFields(userId, path, null, null);
            case "resume" -> userRepository.updateFileFields(userId, null, null, path);
            case "photo" -> userRepository.updateFileFields(userId, null, path, null);
        }
    }
}
