package backend.academy.diplom.DTO.course;

import java.util.Date;

public record OwnedCourseDTO(Long courseId,
                             String name,
                             String courseDate,
                             Integer progress) {
}
