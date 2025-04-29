package backend.academy.diplom.services;

import backend.academy.diplom.DTO.ProjectDTO;
import backend.academy.diplom.DTO.RetLikesDTO;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.ProjectRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.profile.SectionAndStampRepository;
import backend.academy.diplom.repositories.profile.SoftwareSkillRepository;
import backend.academy.diplom.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProjectService {
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;
    private final FileService fileService;
    private final SectionAndStampRepository sectionAndStampRepository;
    private final SoftwareSkillRepository softwareSkillRepository;

    @Transactional
    public void updateLike(String authHeader, Long projectId) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);

        projectRepository.updateLike(user.getId(), projectId);
    }

    public List<RetLikesDTO> getLikes(Long projectId) {
        List<User> users = userRepository.getAllUsersWhoLikeProject(projectId);

        return users.stream().map(user -> {
            String photoPath = user.getPhotoPath();
            if (photoPath != null && !photoPath.isEmpty()) {
                photoPath = fileService.getPresignedLink(photoPath);
            }
            String fullName = String.format("%s %s", user.getSurname(), user.getName());
            return new RetLikesDTO(user.getId(), photoPath, fullName);
        }).toList();
    }

    public List<ProjectDTO> getProjects(String filter, Integer count) {
        switch (filter) {
            case "new" -> {
                return projectToDTO(projectRepository.getNewProjects(count));
            }
            case "popularity" -> {
                return projectToDTO(projectRepository.getPopularityProjects(count));
            }
            case null, default -> {
                return projectToDTO(projectRepository.getAllProjects());
            }
        }
    }

    private List<ProjectDTO> projectToDTO(List<Project> projects) {
        return projects.stream().map(project -> {
            String photoUrl = project.getPhotoUrl();
            Long projectId = project.getId();
            if (photoUrl != null && !photoUrl.isEmpty()) {
                photoUrl = fileService.getPresignedLink(photoUrl);
            }
            List<String> sectionAndStamps = sectionAndStampRepository.getSectionAndStampByProjectId(projectId);
            List<String> softwareSkills = softwareSkillRepository.getSoftwareSkillByProjectId(projectId);
            sectionAndStamps.addAll(softwareSkills);
            Collections.sort(softwareSkills);
            User user = userRepository.findById(project.getAuthorId()).getFirst();
            String fullName = String.format("%s %s", user.getSurname(), user.getName());
            return new ProjectDTO(projectId, photoUrl, softwareSkills, project.getName(), user.getId(), fullName,
                    project.getLikeCount());
        }).toList();
    }

    public boolean isLike(Long projectId, String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        return projectRepository.isLike(projectId, user.getId());
    }

    public Integer getProjectLikeCount(Long projectId) {
        return projectRepository.getProjectLikeCount(projectId);
    }
}
