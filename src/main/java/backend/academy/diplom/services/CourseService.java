package backend.academy.diplom.services;

import backend.academy.diplom.DTO.course.*;
import backend.academy.diplom.entities.*;
import backend.academy.diplom.entities.Module;
import backend.academy.diplom.repositories.*;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.courses.CourseRepository;
import backend.academy.diplom.repositories.courses.TagRepository;
import backend.academy.diplom.repositories.lesson.LessonRepository;
import backend.academy.diplom.repositories.lesson.LessonUserRepository;
import backend.academy.diplom.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    private final JwtUtils jwtUtils;
    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;
    private final LessonUserRepository lessonUserRepository;
    private final TagRepository tagRepository;

    public List<Course> getCoursesOwned(String authHeader) {
        return courseRepository.getAllCoursesByEmail(jwtUtils.getUserByAuthHeader(authHeader).getEmail());
    }

    public ProgramDTO getCourseProgram(String courseName, String authHeader) {
        Map<String, List<LessonDTO>> retProgram = new TreeMap<>();
        List<Module> modules = moduleRepository.getModulesByCourseName(courseName);

        User current = jwtUtils.getUserByAuthHeader(authHeader);

        LessonUser isHereLessonUser = lessonUserRepository.getIsHereByCourseNameAndUserId(courseName, current.getId());
        Long isHereLessonId;
        if (isHereLessonUser != null) {
            isHereLessonId = isHereLessonUser.getLessonId();
        } else {
            isHereLessonId = null;
        }


        for (Module module : modules) {
            List<Lesson> moduleLesson = lessonRepository.getLessonsByModuleId(module.getId());
            List<LessonDTO> moduleLessonDTOs = moduleLesson.stream().map(
                    lesson -> {
                User author = userRepository.findById(lesson.getAuthorId()).getFirst();

                String userName = String.format("%s %s", author.getName(), author.getSurname());
                Long lessonId = lesson.getId();

                return new LessonDTO(lessonId, lesson.getName(), userName,
                        Objects.equals(isHereLessonId, lessonId));
            }).toList();

            retProgram.put(String.format("Модуль %d. %s", module.getNumber(), module.getName()),
                    moduleLessonDTOs);
        }

        return new ProgramDTO(retProgram);
    }

    public LastSeenProgramDTO getLastSeenModule(String courseName, String authHeader) {
        User current = jwtUtils.getUserByAuthHeader(authHeader);
        Long currentUserId = current.getId();

        Map<String, LessonDTO> retMap = new HashMap<>();
        List<Module> isHereModuleList = moduleRepository.getIsHereModuleByCourseNameAndUserId(courseName, currentUserId);
        if (isHereModuleList.isEmpty()) {
            return null;
        }
        Module isHereModule = isHereModuleList.getFirst();

        Lesson isHereLesson = lessonRepository.getIsHereLesson(isHereModule.getId(), currentUserId)
                .getFirst();

        User author = userRepository.findById(isHereLesson.getAuthorId()).getFirst();
        String authorName = String.format("%s %s", author.getName(), author.getSurname());

        String moduleName = String.format("Модуль %d. %s", isHereModule.getNumber(), isHereModule.getName());

        retMap.put(moduleName, new LessonDTO(isHereLesson.getId(),
                isHereLesson.getName(), authorName, true));

        return new LastSeenProgramDTO(retMap);
    }

    public String getCourseModuleName(Long lessonId) {
        return courseRepository.getCourseModuleName(lessonId);
    }

    public Long getBeforeLesson(Long lessonId) {
        return courseRepository.getBeforeLesson(lessonId);
    }

    public Long getAfterLesson(Long lessonId) {
        return courseRepository.getAfterLesson(lessonId);
    }

    public List<CoursePreviewDTO> getMainPreview() {
        List<Course> mainCourses = courseRepository.getMainPreview();

        return convertCourseToCoursePreviewDTO(mainCourses);
    }

    public List<CoursePreviewDTO> getAllPreview() {
        List<Course> allCourses = courseRepository.getAllPreview();

        return convertCourseToCoursePreviewDTO(allCourses);
    }

    private List<CoursePreviewDTO> convertCourseToCoursePreviewDTO(List<Course> courses) {
        return courses.stream().map(course -> {
            List<String> allCourseTags = tagRepository.getAllCourseTags(course.getId());
            return new CoursePreviewDTO(course.getId(), course.getName(),
                    course.getPrice(), course.getDuration(), course.getStartDate(),
                    course.getHours(), allCourseTags);
        }).toList();
    }

    public CourseDetail getCourseDetail(Long courseId) {
        Course courseById = courseRepository.getCourseById(courseId);
        return new CourseDetail(courseById.getName(), courseById.getFormat(),
                courseById.getPriceFull(), courseById.getDuration(), courseById.getWhoWhom(), courseById.getWhatMaster());

    }
}
