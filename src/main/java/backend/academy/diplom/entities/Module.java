package backend.academy.diplom.entities;

import lombok.Data;

@Data
public class Module {
    private Long id;
    private int number;
    private String name;
    private Long courseId;
}
