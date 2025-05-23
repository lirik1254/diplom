package backend.academy.diplom.DTO.lesson;

public record LessonDTO(
        Long lessonId,
        String lessonText,
        String lessonVideoPath,
        String lessonTaskText,
        String lessonNameWithNumber
) {
}
