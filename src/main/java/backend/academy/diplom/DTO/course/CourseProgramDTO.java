package backend.academy.diplom.DTO.course;

import backend.academy.diplom.DTO.lesson.LessonPreviewDTO;

import java.util.List;

public record CourseProgramDTO(
        String moduleName,
        List<LessonPreviewDTO> lessonPreviewDTOS
) {
}
