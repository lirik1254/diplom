package backend.academy.diplom.DTO;

import java.time.LocalDateTime;

public record NotificationDTO(String photoPath, String text, LocalDateTime dateTime,
                              Long userId, Long projectId) {}
