package backend.academy.diplom.DTO.course;

import java.util.Map;

public record LastSeenProgramDTO(Map<String, LessonDTO> lastSeenProgram) {
}
