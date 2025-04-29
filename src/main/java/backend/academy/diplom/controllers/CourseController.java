package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.course.CourseDetail;
import backend.academy.diplom.DTO.course.CoursePreviewDTO;
import backend.academy.diplom.DTO.course.LastSeenProgramDTO;
import backend.academy.diplom.DTO.course.ProgramDTO;
import backend.academy.diplom.entities.Course;
import backend.academy.diplom.services.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/courses")
public class CourseController {
    private final CourseService courseService;

    @GetMapping("/main-preview")
    public List<CoursePreviewDTO> getMainPreview() {
        return courseService.getMainPreview();
    }

    @GetMapping("/all-preview")
    public List<CoursePreviewDTO> getAllPreview() {
        return courseService.getAllPreview();
    }

    @GetMapping("/course-detail/{courseId}")
    public CourseDetail getCourseDetail(@PathVariable Long courseId) {
        return courseService.getCourseDetail(courseId);
    }

    @GetMapping("/owned")
    public List<Course> getOwnedCourse(@RequestHeader("Authorization") String authHeader) {
        return courseService.getCoursesOwned(authHeader);
    }

    @GetMapping("/course-program")
    public ProgramDTO getCourseProgram(@RequestHeader("Authorization") String authHeader,
                                       @RequestParam String courseName) {
        return courseService.getCourseProgram(courseName, authHeader);
    }

    @GetMapping("/last-seen-module")
    public LastSeenProgramDTO getLastSeenModule(@RequestHeader("Authorization") String authHeader,
                                                @RequestParam String courseName) {
        return courseService.getLastSeenModule(courseName, authHeader);
    }

    @GetMapping("/lesson-number")
    public String getLessonNumber(@RequestParam Long lessonId) {
        return courseService.getCourseModuleName(lessonId);
    }

    @GetMapping("/before-lesson")
    public Long getBeforeLesson(@RequestParam Long lessonId) {
        return courseService.getBeforeLesson(lessonId);
    }

    @GetMapping("/after-lesson")
    public Long getAfterLesson(@RequestParam Long lessonId) {
        return courseService.getAfterLesson(lessonId);
    }
}
