package backend.academy.diplom.entities;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Notification {
    private Long id;
    private Long projectId;
    private Long notificationUserId;
    private String text;
    private LocalDateTime dateTime;
    private Boolean isWatched;
}
