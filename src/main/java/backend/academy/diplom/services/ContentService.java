package backend.academy.diplom.services;

import backend.academy.diplom.DTO.ContentDTO;
import backend.academy.diplom.entities.Content;
import backend.academy.diplom.entities.ProjectContent;
import backend.academy.diplom.entities.user.User;
//import backend.academy.diplom.repositories.content.ContentRepository;
import backend.academy.diplom.repositories.content.ProjectContentRepository;
//import backend.academy.diplom.repositories.lesson.LessonUserRepository;
import backend.academy.diplom.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ContentService {
    private final JwtUtils jwtUtils;
//    private final LessonUserRepository lessonUserRepository;
//    private final ContentRepository contentRepository;
    private final FileService fileService;
    private final ProjectContentRepository projectContentRepository;
//
//    public List<ContentDTO> getLessonContent(String authHeader, Long lessonId) {
//        User user = jwtUtils.getUserByAuthHeader(authHeader);
//        lessonUserRepository.updateLastSeenCourse(user.getId(), lessonId);
//
//        List<Content> content = contentRepository.getContentByLessonId(lessonId, user.getId());
//        List<ContentDTO> retContent = content.stream().map(oneContent -> {
//            String presignedContentUrl = null;
//            String contentUrl = oneContent.getContentUrl();
//            if (contentUrl != null) {
//                presignedContentUrl = fileService.getPresignedLink(contentUrl);
//            }
//            return new ContentDTO(oneContent.getContentType(), oneContent.getContent(),
//            presignedContentUrl, oneContent.getOrder());
//        }).toList();
//
//        return retContent;
//    }

    public List<ProjectContent> getProjectContent(Long projectId) {
        List<ProjectContent> projectContent = projectContentRepository.getProjectContent(projectId);
        return projectContent.stream().peek(dto ->  {
            if (!Objects.equals(dto.getContentUrl(), "") && dto.getContentUrl() != null) {
                dto.setContentUrl(fileService.getPresignedLink(dto.getContentUrl()));
            }
        }).toList();
    }
}
