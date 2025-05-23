package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.ProjectDTO;
import backend.academy.diplom.DTO.RetLikesDTO;
import backend.academy.diplom.entities.Project;
import backend.academy.diplom.services.NotificationService;
import backend.academy.diplom.services.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;
    private final NotificationService notificationService;

    @PostMapping("/like/{projectId}")
    public void putLike(@RequestHeader("Authorization") String authHeader,
                        @PathVariable Long projectId) {
        projectService.updateLike(authHeader, projectId);
        notificationService.sendNotification(authHeader, projectId);
    }

    @GetMapping("/like/{projectId}")
    public List<RetLikesDTO> getLikes(@PathVariable Long projectId) {
        return projectService.getLikes(projectId);
    }

    @GetMapping
    public List<ProjectDTO> getProjects(@RequestParam(required = false) String filter,
                                        @RequestParam(required = false) Integer count) {
        return projectService.getProjects(filter, count);
    }

    @GetMapping("/is-like/{projectId}")
    public boolean isLike(@PathVariable Long projectId,
                          @RequestHeader("Authorization") String authHeader) {
        return projectService.isLike(projectId, authHeader);
    }

    @GetMapping("/like-count/{projectId}")
    public Integer getProjectLikeCount(@PathVariable Long projectId) {
        return projectService.getProjectLikeCount(projectId);
    }

    @GetMapping("/author-projects")
    public List<ProjectDTO> getAuthorProjects(@RequestHeader("Authorization") String authHeader) {
        return projectService.getAuthorProjects(authHeader);
    }
}
