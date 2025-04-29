package backend.academy.diplom.repositories.rowmappers;

import backend.academy.diplom.entities.Notification;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class NotificationRowMapper implements RowMapper<Notification> {
    @Override
    public Notification mapRow(ResultSet rs, int rowNum) throws SQLException {
        Notification notification = new Notification();
        notification.setId(rs.getLong("id"));
        notification.setNotificationUserId(rs.getLong("notification_user_id"));
        notification.setText(rs.getString("notification_text"));
        notification.setDateTime(rs.getTimestamp("date_time").toLocalDateTime());
        notification.setProjectId(rs.getLong("project_id"));

        return notification;
    }
}
