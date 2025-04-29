package backend.academy.diplom.services;

import backend.academy.diplom.DTO.ContentDTO;
import backend.academy.diplom.entities.Content;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.ContentRepository;
import backend.academy.diplom.repositories.lesson.LessonUserRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final LessonUserRepository lessonUserRepository;
    private final ContentRepository contentRepository;
    private final FileService fileService;

    public List<ContentDTO> getLessonContent(String authHeader, Long lessonId) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        lessonUserRepository.updateLastSeenCourse(user.getId(), lessonId);

        List<Content> content = contentRepository.getContentByLessonId(lessonId, user.getId());
        List<ContentDTO> retContent = content.stream().map(oneContent -> {
            String presignedContentUrl = null;
            String contentUrl = oneContent.getContentUrl();
            if (contentUrl != null) {
                presignedContentUrl = fileService.getPresignedLink(contentUrl);
            }
            return new ContentDTO(oneContent.getContentType(), oneContent.getContent(),
            presignedContentUrl, oneContent.getOrder());
        }).toList();

        return retContent;
    }
}
