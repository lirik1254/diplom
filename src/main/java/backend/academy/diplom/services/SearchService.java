package backend.academy.diplom.services;

import backend.academy.diplom.DTO.search.ProjectsDTO;
import backend.academy.diplom.DTO.search.SpecialistGetDTO;
import backend.academy.diplom.DTO.search.SpecialistsDTO;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.ProjectRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.profile.SectionAndStampRepository;
import backend.academy.diplom.repositories.profile.SoftwareSkillRepository;
import backend.academy.diplom.repositories.profile.UserSoftwareSkillRepository;
import backend.academy.diplom.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final SectionAndStampRepository sectionAndStampRepository;
    private final ProjectUtils projectUtils;
    private final UserSoftwareSkillRepository userSoftwareSkillRepository;
    private final FileService fileService;
    private final ProjectRepository projectRepository;

    public List<SpecialistsDTO> getSpecialists(String name, List<String> sectionAndStamps,
                                               List<String> softwareSkills) {
        List<User> users = userRepository.getAllUsers();
        if (users.isEmpty()) {
            return null;
        }
        List<SpecialistGetDTO> specialistGetDTOS = new ArrayList<>();

        users.forEach(user -> {
            Long userId = user.getId();
            List<String> userSectionsAndStamps = sectionAndStampRepository.getSectionAndStampByUserid(userId);
            List<String> userSoftwareSkills = userSoftwareSkillRepository.getSoftwareSkillsByUserId(userId);
            String userPhotoPath = user.getPhotoPath();
            String photoUrl = userPhotoPath != null ? fileService.getPresignedLink(userPhotoPath) : null;

            String fullName = String.format("%s %s", user.getSurname(), user.getName());
            specialistGetDTOS.add(new SpecialistGetDTO(
                    userId,
                    user.getStatus(),
                    photoUrl,
                    fullName,
                    user.getCity(),
                    user.getEducation(),
                    userSectionsAndStamps,
                    userSoftwareSkills
            ));
        });

        return sortSpecialists(name, sectionAndStamps, softwareSkills, specialistGetDTOS);
    }

    public List<ProjectsDTO> getProjects(String name, List<String> sectionAndStamps,
                                         List<String> softwareSkills) {
        List<Project> projects = projectRepository.getAllProjects();
        if (projects == null || projects.isEmpty()) {
            return null;
        }

        List<ProjectsDTO> projectsDTOS = projectUtils.projectsToDTO(projects);

        return sortProjects(name, sectionAndStamps, softwareSkills, projectsDTOS);
    }


    private List<SpecialistsDTO> sortSpecialists(String name, List<String> sectionAndStamps,
                                      List<String> softwareSkills, List<SpecialistGetDTO> toSort) {
        if (name != null) {
            toSort = toSort.stream().filter(dto -> dto.getFullName().toLowerCase()
                    .contains(name.toLowerCase())).toList();
        }
        if (sectionAndStamps != null) {
            toSort = toSort.stream().filter(dto -> new HashSet<>(dto.getSectionAndStamp()).containsAll(sectionAndStamps))
                    .toList();
        }
        if (softwareSkills != null) {
            toSort = toSort.stream().filter(dto -> new HashSet<>(dto.getSoftwareSkill()).containsAll(softwareSkills))
                    .toList();
        }

        List<SpecialistsDTO> retList = toSort.stream().map(specialistGetDTOEl -> new SpecialistsDTO(
                specialistGetDTOEl.getId(),
                specialistGetDTOEl.getStatus(),
                specialistGetDTOEl.getPhotoUrl(),
                specialistGetDTOEl.getFullName(),
                specialistGetDTOEl.getCity(),
                specialistGetDTOEl.getEducation()
        )).toList();

        return retList;
    }

    private List<ProjectsDTO> sortProjects(String name, List<String> sectionAndStamps,
                                           List<String> softwareSkills, List<ProjectsDTO> toSort) {
        if (name != null) {
            toSort = toSort.stream().filter(dto -> dto.name().toLowerCase()
                    .contains(name.toLowerCase())).toList();
        }
        if (sectionAndStamps != null) {
            toSort = toSort.stream().filter(dto -> new HashSet<>(dto.sectionAndStamps())
                            .containsAll(sectionAndStamps)).toList();
        }
        if (softwareSkills != null) {
            toSort = toSort.stream().filter(dto -> new HashSet<>(dto.softwareSkills())
                    .containsAll(softwareSkills)).toList();
        }

        return toSort;
    }
}
