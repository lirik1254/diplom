package backend.academy.diplom.DTO.course;

import backend.academy.diplom.DTO.lesson.LessonPreviewDTO;

import java.util.List;
import java.util.Map;


public record ProgramDTO(Map<String, List<LessonPreviewDTO>> program) {
}
