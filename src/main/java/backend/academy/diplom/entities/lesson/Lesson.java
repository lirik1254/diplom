package backend.academy.diplom.entities.lesson;

import lombok.Data;

@Data
public class Lesson {
    private Long id;
    private String name;
    private Long moduleId;
    private Long authorId;
    private Integer number;
}
