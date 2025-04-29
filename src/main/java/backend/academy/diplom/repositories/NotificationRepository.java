package backend.academy.diplom.repositories;

import backend.academy.diplom.entities.Notification;
import backend.academy.diplom.entities.User;
import backend.academy.diplom.repositories.auth.UserRepository;
import backend.academy.diplom.repositories.rowmappers.NotificationRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepository {
    private final NamedParameterJdbcTemplate template;
    private final ProjectRepository projectRepository;
    private final NotificationRowMapper notificationRowMapper;

    public void addNotification(User whoAddUser, Long whichProjectId) {
        String sql = """
                select 1 from engineers.project_user_like as pul
                where pul.project_id = :projectId
                and pul.user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("projectId", whichProjectId)
                .addValue("userId", whoAddUser.getId());

        Optional<Integer> countOpt;
        try {
            Integer count = template.queryForObject(sql, sqlParameterSource, Integer.class);
            countOpt =  Optional.ofNullable(count);
        } catch (Exception e)
        {
            countOpt = Optional.empty();
        }
        if (countOpt.isPresent() && countOpt.get() == 1) {
            Long authorUserId = projectRepository.getAuthorIdByProjectId(whichProjectId);
            if (authorUserId != whoAddUser.getId()) {
                String fullName = String.format("%s %s", whoAddUser.getSurname(), whoAddUser.getName());
                String projectName = projectRepository.getProjectNameById(whichProjectId);
                String notificationText = String.format("%s оценила ваш проект \"%s\"",
                        fullName, projectName);
                String insertSql = """
                    insert into engineers.notification(notification_text,
                    notification_user_id, project_id, date_time, is_watched)
                    values (:notificationText, :notificationUserId, :notificationProjectId,
                            :dateTime, :isWatched)
                    """;
                SqlParameterSource insertSqlSource = new
                            MapSqlParameterSource()
                        .addValue(
                            "notificationText", notificationText)
                        .addValue("notificationUserId", whoAddUser.getId())
                        .addValue("notificationProjectId", whichProjectId)
                        .addValue("dateTime", LocalDateTime.now())
                        .addValue("isWatched", false);
                KeyHolder keyHolder = new GeneratedKeyHolder();

                try {
                    template.update(insertSql, insertSqlSource, keyHolder, new String[]{"id"});
                } catch (Exception e) {
                    return;
                }
                Long notificationId = keyHolder.getKey().longValue();


                addNotificationUser(authorUserId, notificationId);
            }
        }
    }

    private void addNotificationUser(Long userId, Long notificationId) {
        String sql = """
                insert into engineers.notification_user(user_id, notification_id)
                values(:userId, :notificationId)""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("notificationId", notificationId);

        template.update(sql, sqlParameterSource);
    }

    public List<Notification> findAllByUserId(Long userId) {
        String sql = """
                select n.id, n.notification_user_id, n.notification_text, n.project_id, n.date_time, n.is_watched
                from engineers.notification n
                         join engineers.notification_user nu on n.id = nu.notification_id
                where user_id = :userId;
                """;

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource("userId", userId);

        return template.query(sql, sqlParameterSource, notificationRowMapper);
    }

    public void watchNotification(Long userId, Long projectId) {
        String sql = """
                select id from engineers.notification
                where project_id = :projectId and notification_user_id = :userId""";

        SqlParameterSource sqlParameterSource = new MapSqlParameterSource()
                .addValue("userId", userId)
                .addValue("projectId", projectId);

        Integer id;
        try {
            id = template.queryForObject(sql, sqlParameterSource, Integer.class);
            sql = """
                delete from engineers.notification_user nu
                where nu.notification_id = :id;
                
                delete from engineers.notification n
                where n.id = :id;
                """;

            sqlParameterSource = new MapSqlParameterSource("id", id);
            template.update(sql, sqlParameterSource);
        } catch (Exception ignored) {}
    }
}
