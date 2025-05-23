package backend.academy.diplom.services;

import backend.academy.diplom.DTO.course.*;
import backend.academy.diplom.DTO.lesson.LessonDTO;
import backend.academy.diplom.DTO.lesson.LessonPreviewDTO;
import backend.academy.diplom.entities.*;
import backend.academy.diplom.entities.Module;
import backend.academy.diplom.entities.lesson.Lesson;
import backend.academy.diplom.entities.user.User;
import backend.academy.diplom.entities.user.UserCourse;
import backend.academy.diplom.repositories.*;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.courses.CourseRepository;
import backend.academy.diplom.repositories.courses.TagRepository;
import backend.academy.diplom.repositories.courses.UserCourseRepository;
import backend.academy.diplom.repositories.lesson.LessonRepository;
//import backend.academy.diplom.repositories.lesson.LessonUserRepository;
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
//    private final LessonUserRepository lessonUserRepository;
    private final TagRepository tagRepository;
    private final UserCourseRepository userCourseRepository;
    private final FileService fileService;

    public List<OwnedCourseDTO> getCoursesOwned(String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        Long userId = user.getId();
        List<Course> userCourses = courseRepository.getAlLCoursesByUserId(userId);
        List<OwnedCourseDTO> retCourses = new ArrayList<>();

        userCourses.forEach(course -> {
            Long courseId = course.getId();
            List<Long> lessonIds = new ArrayList<>();

            List<Module> modulesByCourseId = moduleRepository.getModulesByCourseId(courseId);
            modulesByCourseId.sort(Comparator.comparing(Module::getNumber));

            modulesByCourseId.forEach(module -> {
                List<Lesson> lessonsByModuleId = lessonRepository.getLessonsByModuleId(module.getId());
                lessonsByModuleId.sort(Comparator.comparing(Lesson::getNumber));

                lessonsByModuleId.forEach(lesson -> lessonIds.add(lesson.getId()));
            });

            Long lastLesson = userCourseRepository.getUserCourseByUserAndCourseId(userId, courseId)
                    .getLastLessonId();

            double progress = lessonIds.indexOf(lastLesson);
            progress = ++progress / lessonIds.size() * 100;

            retCourses.add(new OwnedCourseDTO(courseId, course.getName(), course.getShowDateBoughtCourse(), (int) progress));
        });

        return retCourses;
    }

//    public ProgramDTO getCourseProgram(String courseName, String authHeader) {
//        Map<String, List<LessonDTO>> retProgram = new TreeMap<>();
//        List<Module> modules = moduleRepository.getModulesByCourseName(courseName);
//
//        User current = jwtUtils.getUserByAuthHeader(authHeader);
//
//        LessonUser isHereLessonUser = lessonUserRepository.getIsHereByCourseNameAndUserId(courseName, current.getId());
//        Long isHereLessonId;
//        if (isHereLessonUser != null) {
//            isHereLessonId = isHereLessonUser.getLessonId();
//        } else {
//            isHereLessonId = null;
//        }
//
//
//        for (Module module : modules) {
//            List<Lesson> moduleLesson = lessonRepository.getLessonsByModuleId(module.getId());
//            List<LessonDTO> moduleLessonDTOs = moduleLesson.stream().map(
//                    lesson -> {
//                User author = userRepository.findById(lesson.getAuthorId()).getFirst();
//
//                String userName = String.format("%s %s", author.getName(), author.getSurname());
//                Long lessonId = lesson.getId();
//
//                return new LessonDTO(lessonId, lesson.getName(), userName,
//                        Objects.equals(isHereLessonId, lessonId));
//            }).toList();
//
//            retProgram.put(String.format("Модуль %d. %s", module.getNumber(), module.getName()),
//                    moduleLessonDTOs);
//        }
//
//        return new ProgramDTO(retProgram);
//    }
//
//    public LastSeenProgramDTO getLastSeenModule(String courseName, String authHeader) {
//        User current = jwtUtils.getUserByAuthHeader(authHeader);
//        Long currentUserId = current.getId();
//
//        Map<String, LessonDTO> retMap = new HashMap<>();
//        List<Module> isHereModuleList = moduleRepository.getIsHereModuleByCourseNameAndUserId(courseName, currentUserId);
//        if (isHereModuleList.isEmpty()) {
//            return null;
//        }
//        Module isHereModule = isHereModuleList.getFirst();
//
//        Lesson isHereLesson = lessonRepository.getIsHereLesson(isHereModule.getId(), currentUserId)
//                .getFirst();
//
//        User author = userRepository.findById(isHereLesson.getAuthorId()).getFirst();
//        String authorName = String.format("%s %s", author.getName(), author.getSurname());
//
//        String moduleName = String.format("Модуль %d. %s", isHereModule.getNumber(), isHereModule.getName());
//
//        retMap.put(moduleName, new LessonDTO(isHereLesson.getId(),
//                isHereLesson.getName(), authorName, true));
//
//        return new LastSeenProgramDTO(retMap);
//    }

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

    public CourseDetailDTO getCourseDetail(Long courseId) {
        Course courseById = courseRepository.getCourseById(courseId);
        return new CourseDetailDTO(courseById.getName(), courseById.getFormat(),
                courseById.getPriceFull(), courseById.getDuration(), courseById.getWhoWhom(), courseById.getWhatMaster());

    }

    public List<CourseProgramDTO> getCourseProgram(Long courseId) {
        List<CourseProgramDTO> coursePrograms = new ArrayList<>();
        List<Module> courseModules = moduleRepository.getModulesByCourseId(courseId);
        courseModules.sort(Comparator.comparing(Module::getNumber));

        courseModules.forEach(module -> {
            List<Lesson> moduleLessons = lessonRepository.getLessonsByModuleId(module.getId());
            moduleLessons.sort(Comparator.comparing(Lesson::getNumber));
            List<LessonPreviewDTO> lessonsDTO = new ArrayList<>();

            moduleLessons.forEach(lesson -> {
                User author = userRepository.findById(lesson.getAuthorId()).getFirst();
                String authorPhotoPath = author.getPhotoPath();
                if (authorPhotoPath != null && !Objects.equals("", authorPhotoPath)) {
                    authorPhotoPath = fileService.getPresignedLink(authorPhotoPath);
                }
                String authorName = author.getSurname() + " " + author.getName();
                lessonsDTO.add(new LessonPreviewDTO(lesson.getId(), lesson.getName(), authorName, authorPhotoPath));
            });

            coursePrograms.add(new CourseProgramDTO(module.getName(), lessonsDTO));
        });

        return coursePrograms;
    }

    public CourseProgramDTO getLastSeenProgram(Long courseId, String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        Long userId = user.getId();

        UserCourse userCourse = userCourseRepository.getUserCourseByUserAndCourseId(userId, courseId);
        Long lastLessonId = userCourse.getLastLessonId();

        Lesson lesson = lessonRepository.getLessonByLessonId(lastLessonId);
        Long moduleId = lesson.getModuleId();

        Module module = moduleRepository.getModuleByModuleId(moduleId);

        List<Lesson> moduleLessons = new ArrayList<>(lessonRepository.getLessonsByModuleId(moduleId)
                .stream().filter(getLesson -> getLesson.getNumber() <= lesson.getNumber())
                .toList());

        moduleLessons.sort(Comparator.comparing(Lesson::getNumber));

        List<LessonPreviewDTO> retLessons = moduleLessons.stream().map(getLesson -> {
            Long authorId = getLesson.getAuthorId();
            User author = userRepository.findById(authorId).getFirst();
            String authorPhotoPath = author.getPhotoPath();
            if (authorPhotoPath != null && !Objects.equals("", authorPhotoPath)) {
                authorPhotoPath = fileService.getPresignedLink(authorPhotoPath);
            }
            String authorName = author.getSurname() + " " + author.getName();
            return new LessonPreviewDTO(getLesson.getId(), getLesson.getName(), authorName, authorPhotoPath);
        }).toList();

        return new CourseProgramDTO(module.getName(), retLessons);
    }
}
