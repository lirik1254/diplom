package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.lesson.LessonDTO;
import backend.academy.diplom.DTO.lesson.LessonId;
import backend.academy.diplom.services.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/lesson")
public class LessonController {
    private final LessonService lessonService;

    @GetMapping("/{lessonId}/content")
    public LessonDTO getLessonContent(@PathVariable Long lessonId) {
        return lessonService.getLessonContent(lessonId);
    }

    @PostMapping("/update-last-lesson/{lessonId}")
    public void updateLastLesson(@PathVariable Long lessonId,
                                 @RequestHeader("Authorization") String authToken) {
        lessonService.updateLastLesson(lessonId, authToken);
    }

    @GetMapping("/before-lesson/{lessonId}")
    public LessonId getBeforeLesson(@PathVariable Long lessonId) {
        return lessonService.getBeforeLesson(lessonId);
    }

    @GetMapping("/after-lesson/{lessonId}")
    public LessonId getAfterLesson(@PathVariable Long lessonId) {
        return lessonService.getAfterLesson(lessonId);
    }
}
