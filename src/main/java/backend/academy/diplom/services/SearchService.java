package backend.academy.diplom.services;

import backend.academy.diplom.DTO.ProjectDTO;
import backend.academy.diplom.DTO.search.ProjectsDTO;
import backend.academy.diplom.DTO.search.RecommendDTO;
import backend.academy.diplom.DTO.search.SpecialistGetDTO;
import backend.academy.diplom.DTO.search.SpecialistsDTO;
import backend.academy.diplom.clients.RecommendClient;
import backend.academy.diplom.entities.Company;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.CompanyRepository;
import backend.academy.diplom.repositories.ProjectRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.profile.SectionAndStampRepository;
import backend.academy.diplom.repositories.profile.UserEducationRepository;
import backend.academy.diplom.repositories.profile.UserSoftwareSkillRepository;
import backend.academy.diplom.utils.JwtUtils;
import backend.academy.diplom.utils.ProjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {
    private final UserRepository userRepository;
    private final SectionAndStampRepository sectionAndStampRepository;
    private final ProjectUtils projectUtils;
    private final UserSoftwareSkillRepository userSoftwareSkillRepository;
    private final FileService fileService;
    private final ProjectRepository projectRepository;
    private final CompanyRepository companyRepository;
    private final RecommendClient recommendClient;
    private final UserEducationRepository userEducationRepository;
    private final JwtUtils jwtUtils;

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
            String education = String.join("\n", userEducationRepository.getEducationByUserId(userId));

            String fullName = String.format("%s %s", user.getSurname(), user.getName());
            specialistGetDTOS.add(new SpecialistGetDTO(
                    userId,
                    user.getStatus(),
                    photoUrl,
                    fullName,
                    user.getCity(),
                    education,
                    userSectionsAndStamps,
                    userSoftwareSkills
            ));
        });

        return sortSpecialists(name, sectionAndStamps, softwareSkills, specialistGetDTOS);
    }

    public List<ProjectDTO> getProjects(String name, List<String> sectionAndStamps,
                                        List<String> softwareSkills, String authHeader) {
        List<Project> projects = projectRepository.getAllProjects();
        if (projects == null || projects.isEmpty()) {
            return null;
        }

        User user = jwtUtils.getUserByAuthHeader(authHeader);
        Long userId = user.getId();

        List<RecommendDTO> recommendProjectIds = recommendClient.getRecommendProjectIds(userId);

        Map<Long, Project> projectMap = projects.stream()
                .collect(Collectors.toMap(Project::getId, p -> p));

        projects.clear();
        recommendProjectIds.forEach(recommendDTO ->
                projects.add(projectMap.get(recommendDTO.projectId())));

        List<ProjectsDTO> projectsDTOS = projectUtils.projectsToDTO(projects);

        projectsDTOS = sortProjects(name, sectionAndStamps, softwareSkills, projectsDTOS);
        return projectUtils.toProjectDTO(projectsDTOS);
    }


    public List<Company> getCompanies(String name) {
        List<Company> companies = companyRepository.getCompanies();
        companies = companies.stream().peek(company -> {
            String photoPath = company.getPhotoPath();

            company.setPhotoPath(photoPath == null
                    ? null : fileService.getPresignedLink(photoPath));

        }).toList();
        return sortCompanies(companies, name);
    }

    private List<SpecialistsDTO> sortSpecialists(String name, List<String> sectionAndStamps,
                                      List<String> softwareSkills, List<SpecialistGetDTO> toSort) {
        List<String> temp = new ArrayList<>();

        if (sectionAndStamps != null) {
            temp.addAll(sectionAndStamps);
        }
        if (softwareSkills != null) {
            temp.addAll(softwareSkills);
        }

        if (name != null && !name.isEmpty()) {
            toSort = toSort.stream().filter(dto -> dto.getFullName().toLowerCase()
                    .contains(name.toLowerCase())).toList();
        }

        return toSort.stream().filter(specialistGetDTO -> {
            List<String> elTemp = new ArrayList<>();
            elTemp.addAll(specialistGetDTO.getSoftwareSkill());
            elTemp.addAll(specialistGetDTO.getSectionAndStamp());

            return temp.isEmpty() || elTemp.stream().anyMatch(temp::contains);
        }).map(specialistGetDTOEl -> new SpecialistsDTO(
                specialistGetDTOEl.getId(),
                specialistGetDTOEl.getStatus(),
                specialistGetDTOEl.getPhotoUrl(),
                specialistGetDTOEl.getFullName(),
                specialistGetDTOEl.getCity(),
                specialistGetDTOEl.getEducation()
        )).toList();
    }

    private List<ProjectsDTO> sortProjects(String name, List<String> sectionAndStamps,
                                           List<String> softwareSkills, List<ProjectsDTO> toSort) {
        List<String> temp = new ArrayList<>();

        if (sectionAndStamps != null) {
            temp.addAll(sectionAndStamps);
        }
        if (softwareSkills != null) {
            temp.addAll(softwareSkills);
        }

        if (name != null && !name.isEmpty()) {
            toSort = toSort
                    .stream()
                    .filter(projectsDTO -> projectsDTO.name().toLowerCase().contains(name.toLowerCase()))
                    .toList();
        }

        return toSort.stream().filter(projectsDTO -> {
            List<String> elTemp = new ArrayList<>();
            elTemp.addAll(projectsDTO.softwareSkills());
            elTemp.addAll(projectsDTO.sectionAndStamps());

            return temp.isEmpty() || elTemp.stream().anyMatch(temp::contains);
        }).toList();
    }

    private List<Company> sortCompanies(List<Company> companies, String name) {
        if (name != null && !name.isEmpty()) {
            return companies.stream().filter(company -> company
                    .getName()
                    .toLowerCase()
                    .contains(name.toLowerCase())).toList();
        }
        return companies;
    }

}
