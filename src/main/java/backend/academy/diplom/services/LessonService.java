package backend.academy.diplom.services;

import backend.academy.diplom.DTO.lesson.LessonDTO;
import backend.academy.diplom.DTO.lesson.LessonId;
import backend.academy.diplom.entities.Course;
import backend.academy.diplom.entities.Module;
import backend.academy.diplom.entities.lesson.Lesson;
import backend.academy.diplom.entities.lesson.LessonContent;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.repositories.ModuleRepository;
import backend.academy.diplom.repositories.courses.CourseRepository;
import backend.academy.diplom.repositories.courses.UserCourseRepository;
import backend.academy.diplom.repositories.lesson.LessonContentRepository;
import backend.academy.diplom.repositories.lesson.LessonRepository;
import backend.academy.diplom.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LessonService {
    private final LessonContentRepository lessonContentRepository;
    private final FileService fileService;
    private final UserCourseRepository userCourseRepository;
    private final JwtUtils jwtUtils;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;

    public LessonDTO getLessonContent(Long lessonId) {
        Lesson lesson = lessonRepository.getLessonByLessonId(lessonId);
        String pageName  = lesson.getName().substring(0, 8);
        LessonContent lessonContent = lessonContentRepository.getLessonContentByLessonId(lessonId);
        String videoUrl = fileService.getPresignedLinkIfExist(lessonContent.getVideoUrl());
        return new LessonDTO(lessonContent.getLessonId(),
                lessonContent.getLessonText(),
                videoUrl,
                lessonContent.getLessonTaskText(),
                pageName);
    }

    public void updateLastLesson(Long lessonId, String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        Course course = courseRepository.getCourseByLessonId(lessonId);
        Long courseId = course.getId();

        List<Long> lessonIds = getLessonIds(courseId);

        Long lastLessonId =
                userCourseRepository.getUserCourseByUserAndCourseId(user.getId(), courseId).getLastLessonId();

        if (lessonIds.indexOf(lessonId) > lessonIds.indexOf(lastLessonId)) {
            userCourseRepository.updateLastLesson(user.getId(), courseId, lessonId);
        }
    }

    public LessonId getBeforeLesson(Long lessonId) {
        Course course = courseRepository.getCourseByLessonId(lessonId);
        Long courseId = course.getId();

        List<Long> lessonIds = getLessonIds(courseId);

        int beforeIndex = lessonIds.indexOf(lessonId) - 1;

        return beforeIndex <= 0 ? new LessonId(lessonIds.getFirst()) : new LessonId(lessonIds.get(beforeIndex));
    }


    public LessonId getAfterLesson(Long lessonId) {
        Course course = courseRepository.getCourseByLessonId(lessonId);
        Long courseId = course.getId();

        List<Long> lessonIds = getLessonIds(courseId);

        int afterIndex = lessonIds.indexOf(lessonId) + 1;

        int lessonAvailableIndex = lessonIds.size() - 1;

        return afterIndex >= lessonAvailableIndex
                ? new LessonId(lessonIds.getLast())
                : new LessonId(lessonIds.get(afterIndex));
    }


    private List<Long> getLessonIds(Long courseId) {
        List<Module> courseModules = moduleRepository.getModulesByCourseId(courseId);
        courseModules.sort(Comparator.comparing(Module::getNumber));

        List<Long> lessonIds = new ArrayList<>();

        courseModules.forEach(module -> {
            List<Lesson> moduleLessons = lessonRepository.getLessonsByModuleId(module.getId());
            moduleLessons.sort(Comparator.comparing(Lesson::getNumber));
            moduleLessons.forEach(lesson -> lessonIds.add(lesson.getId()));
        });

        return lessonIds;
    }
}
