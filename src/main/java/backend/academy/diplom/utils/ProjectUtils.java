package backend.academy.diplom.utils;

import backend.academy.diplom.DTO.ProjectDTO;
import backend.academy.diplom.DTO.search.ProjectsDTO;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.profile.SectionAndStampRepository;
import backend.academy.diplom.repositories.profile.SoftwareSkillRepository;
import backend.academy.diplom.services.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ProjectUtils {
    private final UserRepository userRepository;
    private final FileService fileService;
    private final SectionAndStampRepository sectionAndStampRepository;
    private final SoftwareSkillRepository softwareSkillRepository;

    public List<ProjectsDTO> projectsToDTO(List<Project> projects) {
        List<ProjectsDTO> projectsDTOS = new ArrayList<>();

        projects.forEach(project -> {
            User user = userRepository.findById(project.getAuthorId()).getFirst();
            String fullName = String.format("%s %s", user.getSurname(), user.getName());
            String photoUrl = project.getPhotoUrl();
            if (photoUrl != null && photoUrl != "") {
                photoUrl = fileService.getPresignedLink(project.getPhotoUrl());
            }
            Long projectId = project.getId();

            List<String> sectionsAndStampsGet = sectionAndStampRepository.getSectionAndStampByProjectId(projectId);
            List<String> softwareSkillGet = softwareSkillRepository.getSoftwareSkillByProjectId(projectId);

            ProjectsDTO projectsDTO = new ProjectsDTO(project.getId(),
                    project.getName(), project.getAuthorId(), fullName,
                    project.getLikeCount(),
                    photoUrl, sectionsAndStampsGet,
                    softwareSkillGet);

            projectsDTOS.add(projectsDTO);
        });

        return projectsDTOS;
    }

    public List<ProjectDTO> toProjectDTO(List<ProjectsDTO> projectsDTOS) {
        return projectsDTOS.stream()
                .map(projectsDTO -> {
                    projectsDTO.sectionAndStamps().addAll(projectsDTO.softwareSkills());
                    return new ProjectDTO(projectsDTO.id(),
                            projectsDTO.photoUrl(),
                            projectsDTO.sectionAndStamps(),
                            projectsDTO.name(),
                            projectsDTO.authorId(),
                            projectsDTO.fullName(),
                            projectsDTO.likeCount());
                }).toList();
    }
}
