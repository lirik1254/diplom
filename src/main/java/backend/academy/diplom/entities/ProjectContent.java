package backend.academy.diplom.entities;

import lombok.Data;

@Data
public class ProjectContent {
    private Long id;
    private String contentType;
    private String content;
    private String contentUrl;
    private Integer contentOrder;
    private Long projectId;
}
