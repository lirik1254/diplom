package backend.academy.diplom.DTO.course;

import java.util.List;
import java.util.Map;


public record ProgramDTO(Map<String, List<LessonDTO>> program) {
}
