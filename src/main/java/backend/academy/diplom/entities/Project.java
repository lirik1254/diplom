package backend.academy.diplom.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Project {
    private Long id;
    private String name;
    private Long authorId;
    private Integer likeCount;
    private String photoUrl;
    private LocalDateTime dateTime;
}
