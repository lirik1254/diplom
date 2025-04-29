package backend.academy.diplom.entities;

import lombok.Data;

@Data
public class LessonUser {
    private Long lessonId;
    private Long userId;
    private boolean isHere;
}
