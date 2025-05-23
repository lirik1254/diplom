package backend.academy.diplom.DTO.course;

import backend.academy.diplom.DTO.lesson.LessonPreviewDTO;

import java.util.Map;

public record LastSeenProgramDTO(Map<String, LessonPreviewDTO> lastSeenProgram) {
}
