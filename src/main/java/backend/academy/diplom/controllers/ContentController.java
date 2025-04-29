package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.ContentDTO;
import backend.academy.diplom.services.ContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ContentController {
    private final ContentService contentService;

    @GetMapping("/lesson-content/{lessonId}")
    public List<ContentDTO> getLessonContent(@RequestHeader("Authorization") String authHeader,
                                             @PathVariable Long lessonId) {
        return contentService.getLessonContent(authHeader, lessonId);
    }

}
