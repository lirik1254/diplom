package backend.academy.diplom.entities.lesson;

import lombok.Data;

@Data
public class LessonUser {
    private Long lessonId;
    private Long userId;
    private boolean isHere;
}
