package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.course.*;
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
    public CourseDetailDTO getCourseDetail(@PathVariable Long courseId) {
        return courseService.getCourseDetail(courseId);
    }

    @GetMapping("/owned")
    public List<OwnedCourseDTO> getOwnedCourse(@RequestHeader("Authorization") String authHeader) {
        return courseService.getCoursesOwned(authHeader);
    }

    @GetMapping("/course-program/{courseId}")
    public List<CourseProgramDTO> getCourseProgram(@PathVariable Long courseId) {
        return courseService.getCourseProgram(courseId);
    }

    @GetMapping("/last-seen/{courseId}")
    public CourseProgramDTO getLastSeenProgram(@PathVariable Long courseId,
                                                     @RequestHeader("Authorization") String authHeader) {
        return courseService.getLastSeenProgram(courseId, authHeader);
    }

//    @GetMapping("/course-program")
//    public ProgramDTO getCourseProgram(@RequestHeader("Authorization") String authHeader,
//                                       @RequestParam String courseName) {
//        return courseService.getCourseProgram(courseName, authHeader);
//    }
//
//    @GetMapping("/last-seen-module")
//    public LastSeenProgramDTO getLastSeenModule(@RequestHeader("Authorization") String authHeader,
//                                                @RequestParam String courseName) {
//        return courseService.getLastSeenModule(courseName, authHeader);
//    }



}
