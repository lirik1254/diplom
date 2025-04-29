package backend.academy.diplom.services;

import backend.academy.diplom.S3Config;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.utils.CreateAccessToken;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class S3Service {

    private final UserRepository userRepository;
    private final CreateAccessToken createAccessToken;
    private final S3Config s3Config;
    private final S3Client s3Client;

    @Transactional
    public void deleteFile(String authHeader, String type) {
        String token = authHeader.substring(7);

        String email = createAccessToken.getEmailFromJwtToken(token);

        User user = userRepository.findByEmail(email).getFirst();
        Long userId = user.getId();

        String s3Path = "";
        switch (type) {
            case "diploma" -> {
                s3Path = user.getDiplomaPath();
                userRepository.deleteDiploma(userId);
            }
            case "resume" -> {
                s3Path = user.getResumePath();
                userRepository.deleteResume(userId);
            }
            case "photo" -> {
                s3Path = user.getPhotoPath();
                userRepository.deletePhoto(userId);
            }
        }

        if (!Objects.equals(s3Path, "")) {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Config.getYandexCloudBucket())
                    .key(s3Path)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        }
    }
}
