package backend.academy.diplom.entities;

import lombok.Data;

@Data
public class Content {
    private Long id;
    private String contentType;
    private String content;
    private String contentUrl;
    private Integer order;
    private Long lessonId;
}
