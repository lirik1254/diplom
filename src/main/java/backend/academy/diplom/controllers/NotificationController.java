package backend.academy.diplom.controllers;

import backend.academy.diplom.DTO.NotificationDTO;
import backend.academy.diplom.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notification")
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping
    public List<NotificationDTO> getNotification(@RequestHeader("Authorization") String authHeader) {
        return notificationService.getNotifications(authHeader);
    }

    @PostMapping("/watch/{userId}/{projectId}")
    public void watchNotification(@PathVariable Long userId,
                                  @PathVariable Long projectId) {
        notificationService.watchNotification(userId, projectId);
    }
}
