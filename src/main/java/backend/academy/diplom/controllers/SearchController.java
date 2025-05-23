package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.ProjectDTO;
import backend.academy.diplom.DTO.search.ProjectsDTO;
import backend.academy.diplom.DTO.search.SpecialistsDTO;
import backend.academy.diplom.entities.Company;
import backend.academy.diplom.services.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;

    @GetMapping("/specialists")
    public List<SpecialistsDTO> getSpecialists(@RequestParam(required = false) String name,
                                               @RequestParam(required = false) List<String> sectionAndStamp,
                                               @RequestParam(required = false) List<String> softwareSkill) {
        return searchService.getSpecialists(name, sectionAndStamp, softwareSkill);
    }

    @GetMapping("/projects")
    public List<ProjectDTO> getProjects(@RequestParam(required = false) String name,
                                        @RequestParam(required = false) List<String> sectionAndStamp,
                                        @RequestParam(required = false) List<String> softwareSkill,
                                        @RequestHeader("Authorization") String authHeader) {
        return searchService.getProjects(name, sectionAndStamp, softwareSkill, authHeader);
    }

    @GetMapping("/companies")
    public List<Company> getCompanies(@RequestParam(required = false) String name) {
        return searchService.getCompanies(name);
    }

}
