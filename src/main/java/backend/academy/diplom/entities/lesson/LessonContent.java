package backend.academy.diplom.entities.lesson;

import lombok.Data;

@Data
public class LessonContent {
    private Long id;
    private String lessonText;
    private String videoUrl;
    private String lessonTaskText;
    private Long lessonId;
}
