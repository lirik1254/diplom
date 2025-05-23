package backend.academy.diplom.entities.user;

import lombok.Data;

@Data
public class UserCourse {
    private Long userId;
    private Long courseId;
    private Long lastLessonId;
}
