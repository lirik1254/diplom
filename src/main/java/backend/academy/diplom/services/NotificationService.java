package backend.academy.diplom.services;

import backend.academy.diplom.DTO.NotificationDTO;
import backend.academy.diplom.entities.Notification;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.NotificationRepository;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.utils.JwtUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {
    private final JwtUtils jwtUtils;
    private final NotificationRepository notificationRepository;
    private final FileService fileService;
    private final UserRepository userRepository;

    @Transactional
    public void sendNotification(String authHeader, Long projectId) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        notificationRepository.addNotification(user, projectId);
    }

    public List<NotificationDTO> getNotifications(String authHeader) {
        User user = jwtUtils.getUserByAuthHeader(authHeader);
        List<Notification> notifications = notificationRepository.findAllByUserId(user.getId());

        return notifications.stream().map(notification -> {
            User getUser = userRepository.findById(notification.getNotificationUserId()).getFirst();
            String photoPath = getUser.getPhotoPath();
            photoPath = photoPath == null ? null : fileService.getPresignedLink(photoPath);

            NotificationDTO notificationDTO = new NotificationDTO(
                photoPath, notification.getText(), notification.getDateTime(),
                    notification.getNotificationUserId(), notification.getProjectId()
            );
            return notificationDTO;
        }).toList();
    }

    public void watchNotification(Long userId, Long projectId) {
        notificationRepository.watchNotification(userId, projectId);
    }
}
